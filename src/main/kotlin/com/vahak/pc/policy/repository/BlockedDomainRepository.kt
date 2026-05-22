package com.vahak.pc.policy.repository

import com.vahak.pc.policy.domain.BlockedDomain
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface BlockedDomainRepository : JpaRepository<BlockedDomain, UUID> {
    fun findAllByChildId(childId: UUID): List<BlockedDomain>
    fun deleteAllByChildId(childId: UUID)
}