package com.vahak.pc.policy.controller

import com.vahak.pc.policy.domain.AppRule
import com.vahak.pc.policy.dto.AppRuleDto
import com.vahak.pc.policy.dto.BulkRuleRequest
import com.vahak.pc.policy.repository.AppRuleRepository
import org.springframework.http.ResponseEntity
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.*
import java.time.Instant
import java.util.UUID

@RestController
@RequestMapping("/v1/rules")
class AppRuleController(
    private val appRuleRepository: AppRuleRepository
) {

    @GetMapping("/{childId}")
    fun getRules(@PathVariable childId: UUID): ResponseEntity<List<AppRuleDto>> {
        val rules = appRuleRepository.findAllByChildId(childId)
        val responseList = rules.map {
            AppRuleDto(it.packageName, it.isAllowed, it.updatedAt?.toEpochMilli() ?: 0L)
        }
        return ResponseEntity.ok(responseList)
    }

    @PutMapping("/{childId}/bulk")
    @Transactional
    fun updateRules(
        @PathVariable childId: UUID,
        @RequestBody request: BulkRuleRequest
    ): ResponseEntity<Unit> {
        val existingRules = appRuleRepository.findAllByChildId(childId).associateBy { it.packageName }
        val rulesToSave = mutableListOf<AppRule>()

        for (dto in request.rules) {
            val existingRule = existingRules[dto.packageName]
            val incomingTime = Instant.ofEpochMilli(dto.updatedAt)

            if (existingRule != null) {
                // LAST-WRITE-WINS
                if (existingRule.updatedAt == null || incomingTime.isAfter(existingRule.updatedAt)) {
                    existingRule.isAllowed = dto.isAllowed
                    existingRule.updatedAt = incomingTime
                    rulesToSave.add(existingRule)
                }
            } else {
                rulesToSave.add(
                    AppRule(childId = childId, packageName = dto.packageName, isAllowed = dto.isAllowed, updatedAt = incomingTime)
                )
            }
        }

        appRuleRepository.saveAll(rulesToSave)
        return ResponseEntity.ok().build()
    }
}