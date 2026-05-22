package com.vahak.pc.policy.repository

import com.vahak.pc.policy.domain.AppRule
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface AppRuleRepository : JpaRepository<AppRule, UUID> {
    fun findAllByChildId(childId: UUID): List<AppRule>
    fun deleteAllByChildId(childId: UUID)
}