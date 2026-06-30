package com.vahak.pc.policy.controller

import com.vahak.pc.policy.dto.BrowserPolicySyncRequestDto
import com.vahak.pc.policy.dto.BrowserSyncResponseDto
import com.vahak.pc.policy.service.BrowserSettingService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/v1/browser")
class BrowserSettingController(
    private val policyService: BrowserSettingService
) {

    @PutMapping("/{childId}/sync")
    fun syncBrowserPolicy(
        @PathVariable childId: String, @RequestBody request: BrowserPolicySyncRequestDto
    ): ResponseEntity<Void> {
        policyService.processBrowserSync(childId, request)
        return ResponseEntity.ok().build()
    }

    @GetMapping("/{childId}")
    fun getBrowserSettings(
        @PathVariable childId: String
    ): ResponseEntity<BrowserSyncResponseDto> {
        return ResponseEntity.ok(policyService.getFullBrowserProfile(childId))
    }
}