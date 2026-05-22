package com.vahak.pc.policy.controller

import com.vahak.pc.policy.domain.BlockedDomain
import com.vahak.pc.policy.dto.BlockedDomainDto
import com.vahak.pc.policy.dto.BulkDomainRequest
import com.vahak.pc.policy.repository.BlockedDomainRepository
import org.springframework.http.ResponseEntity
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.*
import java.time.Instant
import java.util.UUID

@RestController
@RequestMapping("/v1/domains")
class BlockedDomainController(
    private val blockedDomainRepository: BlockedDomainRepository
) {

    @GetMapping("/{childId}")
    fun getBlockedDomains(@PathVariable childId: UUID): ResponseEntity<List<BlockedDomainDto>> {
        val domains = blockedDomainRepository.findAllByChildId(childId)
        val responseList = domains.map { 
            BlockedDomainDto(it.id, it.domain, it.isActive, it.updatedAt?.toEpochMilli() ?: 0L) 
        }
        return ResponseEntity.ok(responseList)
    }

    @PutMapping("/{childId}/bulk")
    @Transactional
    fun updateBlockedDomains(
        @PathVariable childId: UUID,
        @RequestBody request: BulkDomainRequest
    ): ResponseEntity<Unit> {
        val existingDomains = blockedDomainRepository.findAllByChildId(childId).associateBy { it.id }
        val domainsToSave = mutableListOf<BlockedDomain>()

        for (dto in request.domains) {
            val existingDomain = existingDomains[dto.id]
            val incomingTime = Instant.ofEpochMilli(dto.updatedAt)

            if (existingDomain != null) {
                // LAST-WRITE-WINS
                if (existingDomain.updatedAt == null || incomingTime.isAfter(existingDomain.updatedAt)) {
                    existingDomain.isActive = dto.isActive
                    existingDomain.updatedAt = incomingTime
                    domainsToSave.add(existingDomain)
                }
            } else {
                domainsToSave.add(
                    BlockedDomain(id = dto.id, childId = childId, domain = dto.domain, isActive = dto.isActive, updatedAt = incomingTime)
                )
            }
        }

        blockedDomainRepository.saveAll(domainsToSave)
        return ResponseEntity.ok().build()
    }

    @DeleteMapping("/{childId}/{domainId}")
    @Transactional
    fun deleteDomain(
        @PathVariable childId: UUID,
        @PathVariable domainId: UUID
    ): ResponseEntity<Unit> {
        // Soft-deleted on Android triggers this hard delete on the server
        if (blockedDomainRepository.existsById(domainId)) {
            blockedDomainRepository.deleteById(domainId)
        }
        return ResponseEntity.ok().build()
    }
}