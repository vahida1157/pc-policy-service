package com.vahak.pc.policy.dto

data class BrowserSettingsDto(
    val searchEngine: String,
    val isCartoonWorldEnabled: Boolean,
    val filterMode: String,
    val updatedAt: Long
)

data class BrowserSiteDto(
    val url: String,
    val label: String?,
    val isActive: Boolean,
    val updatedAt: Long
)

data class BrowserKeywordDto(
    val keyword: String,
    val isActive: Boolean,
    val updatedAt: Long
)

// Incoming Policy Sync (From Android)
data class BrowserPolicySyncRequestDto(
    val settings: BrowserSettingsDto?,
    val allowedSites: List<BrowserSiteDto> = emptyList(),
    val blockedSites: List<BrowserSiteDto> = emptyList(),
    val blockedKeywords: List<BrowserKeywordDto> = emptyList()
)

// Outgoing Policy Sync (To Android)
data class BrowserSyncResponseDto(
    val settings: BrowserSettingsDto?,
    val allowedSites: List<BrowserSiteDto>,
    val blockedSites: List<BrowserSiteDto>,
    val blockedKeywords: List<BrowserKeywordDto>
)