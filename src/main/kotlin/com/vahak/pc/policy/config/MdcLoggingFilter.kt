package com.vahak.pc.policy.config // (And also in telemetry)

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.MDC
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class MdcLoggingFilter : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        // Grab the authentication object Spring magically created
        val authentication = SecurityContextHolder.getContext().authentication

        // If it's a valid JWT, extract the subject (parentId) and inject it into MDC
        if (authentication is JwtAuthenticationToken) {
            val parentId = authentication.token.subject
            MDC.put("userId", parentId)
        }

        try {
            filterChain.doFilter(request, response)
        } finally {
            MDC.clear() // Prevent memory leaks and cross-user contamination
        }
    }
}