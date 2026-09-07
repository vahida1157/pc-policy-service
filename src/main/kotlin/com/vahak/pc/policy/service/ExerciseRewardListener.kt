package com.vahak.pc.policy.service

import com.vahak.pc.policy.repository.GlobalChildSettingsRepository
import org.slf4j.LoggerFactory
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate
import java.util.*

// Matches the exact event payload sent from the Exercise Service
data class ExerciseScoreEarnedEvent(
	val childId: UUID,
	val parentId: UUID,
	val exerciseId: Int,
	val pointsEarned: Int,
	val durationSeconds: Long,
	val recordedAt: String
)

@Service
class ExerciseRewardListener(
	private val settingsRepository: GlobalChildSettingsRepository
) {
	private val log = LoggerFactory.getLogger(javaClass)

	@RabbitListener(queues = ["policy.exercise.score.earned.queue"])
	@Transactional
	fun handleExerciseScore(event: ExerciseScoreEarnedEvent) {
		// 🚀 FIX: Use findByIdOrNull because childId is the @Id Primary Key
		val settings = settingsRepository.findByIdOrNull(event.childId) ?: return

		if (!settings.isExerciseRewardEnabled) {
			log.info("Rewards disabled for child ${event.childId}. Ignoring points.")
			return
		}

		// 1. Reset daily bonus if it's a new day
		val today = LocalDate.now()
		if (settings.lastRewardDate == null || settings.lastRewardDate!!.isBefore(today)) {
			settings.earnedBonusSecondsToday = 0
			settings.lastRewardDate = today
		}

		// 2. Calculate the theoretical time earned (Points * 30 seconds)
		val theoreticalBonus = event.pointsEarned * settings.rewardSecondsPerPoint

		// 3. Enforce the Parent's Maximum Cap (e.g., 2 hours max)
		val remainingCap = settings.maxRewardSecondsPerDay - settings.earnedBonusSecondsToday

		if (remainingCap <= 0) {
			log.info("Child ${event.childId} hit the max daily reward cap. No extra time added.")
			return
		}

		// 4. Grant the time, ensuring we don't exceed the remaining cap
		val actualBonusToGrant = minOf(theoreticalBonus, remainingCap)

		settings.earnedBonusSecondsToday += actualBonusToGrant
		settingsRepository.save(settings)

		log.info("Granted $actualBonusToGrant bonus seconds to child ${event.childId}")
	}
}