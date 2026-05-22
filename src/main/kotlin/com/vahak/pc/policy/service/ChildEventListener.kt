package com.vahak.pc.policy.service

import com.vahak.pc.policy.repository.AppRuleRepository
import com.vahak.pc.policy.repository.BlockedDomainRepository
import com.vahak.pc.policy.repository.GlobalChildSettingsRepository
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class ChildEventListener(
    private val childSettingsService: GlobalChildSettingsService,
    private val childSettingsRepository: GlobalChildSettingsRepository,
    private val appRuleRepository: AppRuleRepository,
    private val blockedDomainRepository: BlockedDomainRepository
) {

    @RabbitListener(queues = ["policy.child.created.queue"])
    fun handleChildCreatedEvent(event: Map<String, String>) {
        val childIdString = event["childId"] ?: return
        val childId = UUID.fromString(childIdString)
        childSettingsService.createDefaultChildSettings(childId)
    }

    // NEW: Handles the deletion cascade
    @RabbitListener(queues = ["policy.child.deleted.queue"]) // Make sure to bind this queue in RabbitConfig!
    @Transactional
    fun handleChildDeletedEvent(event: Map<String, String>) {
        val childIdString = event["childId"] ?: return
        val childId = UUID.fromString(childIdString)

        // Wipe all associated policy data for the deleted child
        childSettingsRepository.deleteById(childId)
        appRuleRepository.deleteAllByChildId(childId)
        blockedDomainRepository.deleteAllByChildId(childId)
    }
}