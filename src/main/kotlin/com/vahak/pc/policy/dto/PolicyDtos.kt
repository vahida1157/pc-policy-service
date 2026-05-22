package com.vahak.pc.policy.dto

import java.time.LocalTime
import java.util.UUID

// --- SETTINGS DTOs ---
data class UpdateSettingsRequest(
    val isChildThemeActive: Boolean,
    val isTimeLimitActive: Boolean,
    val dailyTimeLimitMins: Int,
    val isSleepTimeActive: Boolean,
    val sleepTimeStart: LocalTime,
    val sleepTimeEnd: LocalTime,
    val isSiteManagementActive: Boolean,
    val updatedAt: Long // NEW: For Last-Write-Wins
)

data class GlobalSettingsResponse(
    val childId: String,
    val isChildThemeActive: Boolean,
    val isTimeLimitActive: Boolean,
    val dailyTimeLimitMins: Int,
    val isSleepTimeActive: Boolean,
    val sleepTimeStart: LocalTime,
    val sleepTimeEnd: LocalTime,
    val isSiteManagementActive: Boolean
)

// --- APP RULE DTOs ---
data class AppRuleDto(
    val packageName: String,
    val isAllowed: Boolean,
    val updatedAt: Long // NEW: For Last-Write-Wins
)

data class BulkRuleRequest(
    val rules: List<AppRuleDto>
)

// --- WEB DOMAIN DTOs ---
data class BlockedDomainDto(
    val id: UUID,
    val domain: String,
    val isActive: Boolean,
    val updatedAt: Long // NEW: For Last-Write-Wins
)

data class BulkDomainRequest(
    val domains: List<BlockedDomainDto>
)