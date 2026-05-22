package com.vahak.pc.policy.repository

import com.vahak.pc.policy.domain.GlobalChildSettings
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface GlobalChildSettingsRepository : JpaRepository<GlobalChildSettings, UUID>