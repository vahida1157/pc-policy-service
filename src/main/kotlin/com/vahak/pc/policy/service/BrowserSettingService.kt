package com.vahak.pc.policy.service

import com.vahak.pc.policy.domain.BrowserAllowedSite
import com.vahak.pc.policy.domain.BrowserBlockedKeyword
import com.vahak.pc.policy.domain.BrowserBlockedSite
import com.vahak.pc.policy.domain.BrowserSettings
import com.vahak.pc.policy.dto.*
import com.vahak.pc.policy.repository.BrowserAllowedSiteRepository
import com.vahak.pc.policy.repository.BrowserBlockedKeywordRepository
import com.vahak.pc.policy.repository.BrowserBlockedSiteRepository
import com.vahak.pc.policy.repository.BrowserSettingsRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class BrowserSettingService(
    private val settingsRepo: BrowserSettingsRepository,
    private val allowedRepo: BrowserAllowedSiteRepository,
    private val blockedRepo: BrowserBlockedSiteRepository,
    private val keywordRepo: BrowserBlockedKeywordRepository
) {

    @Transactional
    fun processBrowserSync(childId: String, request: BrowserPolicySyncRequestDto) {

        // 1. Sync Settings (Last-Write-Wins based on timestamp)
        request.settings?.let { incoming ->
            val existing = settingsRepo.findById(childId).orElse(null)
            if (existing == null || incoming.updatedAt > existing.updatedAt) {
                settingsRepo.save(
                    BrowserSettings(
                        childId = childId,
                        searchEngine = incoming.searchEngine,
                        isCartoonWorldEnabled = incoming.isCartoonWorldEnabled,
                        filterMode = incoming.filterMode,
                        updatedAt = incoming.updatedAt
                    )
                )
            }
        }

        // 2. Sync Allowed Sites
        request.allowedSites.forEach { incoming ->
            val existing = allowedRepo.findByChildIdAndUrl(childId, incoming.url)
            if (existing != null) {
                if (incoming.updatedAt > existing.updatedAt) {
                    existing.isActive = incoming.isActive
                    existing.updatedAt = incoming.updatedAt
                    allowedRepo.save(existing)
                }
            } else {
                allowedRepo.save(
                    BrowserAllowedSite(
                        childId = childId,
                        url = incoming.url,
                        label = incoming.label ?: "",
                        isActive = incoming.isActive,
                        updatedAt = incoming.updatedAt
                    )
                )
            }
        }

        // 3. Sync Blocked Sites
        request.blockedSites.forEach { incoming ->
            val existing = blockedRepo.findByChildIdAndUrl(childId, incoming.url)
            if (existing != null) {
                if (incoming.updatedAt > existing.updatedAt) {
                    existing.isActive = incoming.isActive
                    existing.updatedAt = incoming.updatedAt
                    blockedRepo.save(existing)
                }
            } else {
                blockedRepo.save(
                    BrowserBlockedSite(
                        childId = childId,
                        url = incoming.url,
                        isActive = incoming.isActive,
                        updatedAt = incoming.updatedAt
                    )
                )
            }
        }

        // 4. Sync Keywords
        request.blockedKeywords.forEach { incoming ->
            val existing = keywordRepo.findByChildIdAndKeyword(childId, incoming.keyword)
            if (existing != null) {
                if (incoming.updatedAt > existing.updatedAt) {
                    existing.isActive = incoming.isActive
                    existing.updatedAt = incoming.updatedAt
                    keywordRepo.save(existing)
                }
            } else {
                keywordRepo.save(
                    BrowserBlockedKeyword(
                        childId = childId,
                        keyword = incoming.keyword,
                        isActive = incoming.isActive,
                        updatedAt = incoming.updatedAt
                    )
                )
            }
        }
    }

    fun getFullBrowserProfile(childId: String): BrowserSyncResponseDto {
        val settings = settingsRepo.findById(childId).orElse(null)?.let {
            BrowserSettingsDto(it.searchEngine, it.isCartoonWorldEnabled, it.filterMode, it.updatedAt)
        }

        val allowed = allowedRepo.findAllByChildIdAndIsActiveTrue(childId)
            .map { BrowserSiteDto(it.url, it.label, true, it.updatedAt) }
        val blocked = blockedRepo.findAllByChildIdAndIsActiveTrue(childId)
            .map { BrowserSiteDto(it.url, null, true, it.updatedAt) }
        val keywords = keywordRepo.findAllByChildIdAndIsActiveTrue(childId)
            .map { BrowserKeywordDto(it.keyword, true, it.updatedAt) }

        return BrowserSyncResponseDto(settings, allowed, blocked, keywords)
    }
}