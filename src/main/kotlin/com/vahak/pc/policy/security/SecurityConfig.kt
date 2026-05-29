package com.vahak.pc.policy.security

import com.vahak.pc.policy.config.MdcLoggingFilter
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.oauth2.jose.jws.MacAlgorithm
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.security.oauth2.jwt.JwtDecoder
import org.springframework.security.oauth2.jwt.JwtException
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder
import org.springframework.security.oauth2.server.resource.web.authentication.BearerTokenAuthenticationFilter
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.security.web.SecurityFilterChain
import javax.crypto.spec.SecretKeySpec

@Configuration
@EnableWebSecurity
class SecurityConfig(private val mdcLoggingFilter: MdcLoggingFilter) {

    @Value($$"${jwt.secret}")
    private lateinit var jwtSecret: String

    private val logger = LoggerFactory.getLogger(javaClass)

    @Bean
    fun userDetailsService(): UserDetailsService = UserDetailsService { username ->
        throw UsernameNotFoundException("User $username not found. Use JWT authentication.")
    }

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .csrf { it.disable() }
            .cors { it.disable() }
            .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
            .authorizeHttpRequests {
                it.requestMatchers("/v1/**").authenticated()
                it.anyRequest().permitAll()
            }
            .oauth2ResourceServer { oauth2 ->
                oauth2.jwt { jwt -> jwt.decoder(jwtDecoder()) }
            }
            .addFilterAfter(mdcLoggingFilter, BearerTokenAuthenticationFilter::class.java)
            .formLogin { it.disable() }
            .httpBasic { it.disable() }
            .exceptionHandling { it.authenticationEntryPoint(authenticationEntryPoint()) } // optional

        return http.build()
    }

    @Bean
    fun jwtDecoder(): JwtDecoder {
        val secretKey = SecretKeySpec(jwtSecret.toByteArray(), "HmacSHA256")
        val delegate = NimbusJwtDecoder.withSecretKey(secretKey)
            .macAlgorithm(MacAlgorithm.HS256)
            .build()
        return LoggingJwtDecoder(delegate)
    }

    @Bean
    fun authenticationEntryPoint(): AuthenticationEntryPoint =
        AuthenticationEntryPoint { request, response, authException ->
            logger.warn("Authentication failed for ${request.requestURI}: ${authException.message}")
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized")
        }

    // inner class or separate file
    private class LoggingJwtDecoder(private val delegate: JwtDecoder) : JwtDecoder {
        private val log = LoggerFactory.getLogger(javaClass)
        override fun decode(token: String): Jwt {
            return try {
                val jwt = delegate.decode(token)
                log.info("✅ JWT valid for subject: ${jwt.subject}")
                jwt
            } catch (e: JwtException) {
                log.warn("❌ Invalid JWT: ${e.message}")
                throw e
            }
        }
    }
}