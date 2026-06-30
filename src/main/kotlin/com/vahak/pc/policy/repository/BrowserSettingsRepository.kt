package com.vahak.pc.policy.repository

import com.vahak.pc.policy.domain.BrowserAllowedSite
import com.vahak.pc.policy.domain.BrowserBlockedKeyword
import com.vahak.pc.policy.domain.BrowserBlockedSite
import com.vahak.pc.policy.domain.BrowserSettings
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface BrowserSettingsRepository : JpaRepository<BrowserSettings, String>

@Repository
interface BrowserAllowedSiteRepository : JpaRepository<BrowserAllowedSite, Long> {
    fun findByChildIdAndUrl(childId: String, url: String): BrowserAllowedSite?
    fun findAllByChildIdAndIsActiveTrue(childId: String): List<BrowserAllowedSite>
}

@Repository
interface BrowserBlockedSiteRepository : JpaRepository<BrowserBlockedSite, Long> {
    fun findByChildIdAndUrl(childId: String, url: String): BrowserBlockedSite?
    fun findAllByChildIdAndIsActiveTrue(childId: String): List<BrowserBlockedSite>
}

@Repository
interface BrowserBlockedKeywordRepository : JpaRepository<BrowserBlockedKeyword, Long> {
    fun findByChildIdAndKeyword(childId: String, keyword: String): BrowserBlockedKeyword?
    fun findAllByChildIdAndIsActiveTrue(childId: String): List<BrowserBlockedKeyword>
}