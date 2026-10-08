package com.vahak.pc.policy.domain

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.hibernate.annotations.UpdateTimestamp
import java.time.Instant
import java.time.LocalTime
import java.util.*

@Entity
@Table(name = "global_settings")
class GlobalChildSettings(

    @Id
    @Column(name = "child_id", updatable = false, nullable = false)
    val childId: UUID,

    // --- Theme ---
    @Column(name = "is_child_theme_active", nullable = false)
    var isChildThemeActive: Boolean = true,

    // --- Time Limit ---
    @Column(name = "is_time_limit_active", nullable = false)
    var isTimeLimitActive: Boolean = false,

    @Column(name = "daily_time_limit_mins", nullable = false)
    var dailyTimeLimitMins: Int = 60,

    // --- SleepTime Limit ---
    @Column(name = "is_sleep_time_active", nullable = false)
    var isSleepTimeActive: Boolean = false,

    @Column(name = "sleep_time_start", nullable = false)
    var sleepTimeStart: LocalTime = LocalTime.of(22, 0),

    @Column(name = "sleep_time_end", nullable = false)
    var sleepTimeEnd: LocalTime = LocalTime.of(7, 0),

    // --- Web Filter / Site Management ---
    @Column(name = "is_site_management_active", nullable = false)
    var isSiteManagementActive: Boolean = false,

    // --- 🛡️ Ad Blocker ---
    @Column(name = "is_ad_block_active", nullable = false, columnDefinition = "boolean default false")
    var isAdBlockActive: Boolean = false,

    // --- 👁️ Eye Filter ---
    @Column(name = "is_eye_filter_active", nullable = false, columnDefinition = "boolean default false")
    var isEyeFilterActive: Boolean = false,

    @Column(name = "eye_filter_level", nullable = false, length = 20, columnDefinition = "varchar(20) default 'MEDIUM'")
    var eyeFilterLevel: String = "MEDIUM",

    @Column(name = "is_eye_filter_night_boost", nullable = false, columnDefinition = "boolean default true")
    var isEyeFilterNightBoost: Boolean = true,

    // --- ⏳ Break Reminder ---
    @Column(name = "is_break_reminder_active", nullable = false, columnDefinition = "boolean default false")
    var isBreakReminderActive: Boolean = false,

    @Column(name = "break_every_mins", nullable = false, columnDefinition = "integer default 40")
    var breakEveryMins: Int = 40,

    @UpdateTimestamp
    @Column(name = "updated_at")
    var updatedAt: Instant? = null
)