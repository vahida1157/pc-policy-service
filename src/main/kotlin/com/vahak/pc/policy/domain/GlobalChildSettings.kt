package com.vahak.pc.policy.domain

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.hibernate.annotations.UpdateTimestamp
import java.time.Instant
import java.time.LocalDate
import java.time.LocalTime
import java.util.*

@Entity
@Table(name = "global_settings")
class GlobalChildSettings(

	// The Child's UUID from the Identity Service acts as the Primary Key here!
	@Id
	@Column(name = "child_id", updatable = false, nullable = false)
	val childId: UUID,

	// --- Theme ---
	@Column(nullable = false)
	var isChildThemeActive: Boolean = true,

	// --- Time Limit ---
	@Column(nullable = false)
	var isTimeLimitActive: Boolean = false,

	@Column(nullable = false)
	var dailyTimeLimitMins: Int = 60,

	// --- SleepTime Limit ---
	@Column(nullable = false)
	var isSleepTimeActive: Boolean = false,

	@Column(nullable = false)
	var sleepTimeStart: LocalTime = LocalTime.of(22, 0),

	@Column(nullable = false)
	var sleepTimeEnd: LocalTime = LocalTime.of(7, 0),

	// --- Gamification & Rewards ---
	@Column(nullable = false)
	var isExerciseRewardEnabled: Boolean = true,

	@Column(nullable = false)
	var rewardSecondsPerPoint: Int = 3,

	@Column(nullable = false)
	var maxRewardSecondsPerDay: Int = 3600,

	@Column(nullable = false)
	var earnedBonusSecondsToday: Int = 0,

	@Column(nullable = true)
	var lastRewardDate: LocalDate? = null,

	// --- Web Filter / Site Management ---
	@Column(nullable = false)
	var isSiteManagementActive: Boolean = false,

	@UpdateTimestamp
	var updatedAt: Instant? = null
)