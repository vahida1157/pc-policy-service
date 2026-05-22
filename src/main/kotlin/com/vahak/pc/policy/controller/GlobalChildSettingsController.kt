package com.vahak.pc.policy.controller

import com.vahak.pc.policy.dto.GlobalSettingsResponse
import com.vahak.pc.policy.dto.UpdateSettingsRequest
import com.vahak.pc.policy.service.GlobalChildSettingsService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.time.Instant
import java.util.UUID

@RestController
@RequestMapping("/v1/settings")
class GlobalChildSettingsController(
    private val service: GlobalChildSettingsService
) {

    @GetMapping("/{childId}")
    fun getSettings(@PathVariable childId: UUID): ResponseEntity<GlobalSettingsResponse> {
        val settings = service.getChildSettings(childId)
        val response = GlobalSettingsResponse(
            childId = settings.childId.toString(),
            isChildThemeActive = settings.isChildThemeActive,
            isTimeLimitActive = settings.isTimeLimitActive,
            dailyTimeLimitMins = settings.dailyTimeLimitMins,
            isSleepTimeActive = settings.isSleepTimeActive,
            sleepTimeStart = settings.sleepTimeStart,
            sleepTimeEnd = settings.sleepTimeEnd,
            isSiteManagementActive = settings.isSiteManagementActive
        )
        return ResponseEntity.ok(response)
    }

    @PutMapping("/{childId}")
    fun updateSettings(
        @PathVariable childId: UUID,
        @RequestBody request: UpdateSettingsRequest
    ): ResponseEntity<Unit> {
        val settings = service.getChildSettings(childId)
        val incomingTime = Instant.ofEpochMilli(request.updatedAt)

        if (settings.updatedAt == null || incomingTime.isAfter(settings.updatedAt)) {
            settings.isChildThemeActive = request.isChildThemeActive
            settings.isTimeLimitActive = request.isTimeLimitActive
            settings.dailyTimeLimitMins = request.dailyTimeLimitMins
            settings.isSleepTimeActive = request.isSleepTimeActive
            settings.sleepTimeStart = request.sleepTimeStart
            settings.sleepTimeEnd = request.sleepTimeEnd
            settings.isSiteManagementActive = request.isSiteManagementActive
            settings.updatedAt = incomingTime

            service.updateChildSettings(settings)
        }

        return ResponseEntity.ok().build()
    }
}