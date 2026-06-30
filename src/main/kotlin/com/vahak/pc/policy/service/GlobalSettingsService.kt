package com.vahak.pc.policy.service

import com.vahak.pc.policy.domain.GlobalChildSettings
import com.vahak.pc.policy.repository.GlobalChildSettingsRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.util.*

@Service
class GlobalChildSettingsService(
    private val repository: GlobalChildSettingsRepository
) {

    fun createDefaultChildSettings(childId: UUID): GlobalChildSettings {
        if (repository.existsById(childId)) {
            return repository.findById(childId).get()
        }
        return repository.save(GlobalChildSettings(childId = childId))
    }

    fun getChildSettings(childId: UUID): GlobalChildSettings {
        return repository.findByIdOrNull(childId) ?: GlobalChildSettings(childId = childId)
    }

    fun updateChildSettings(settings: GlobalChildSettings): GlobalChildSettings {
        return repository.save(settings)
    }
}