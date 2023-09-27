package com.epam.esm.web.config

import com.epam.esm.model.entity.Role
import com.epam.esm.web.security.SecurityContextRepository
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.security.authentication.ReactiveAuthenticationManager
import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.core.userdetails.ReactiveUserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.server.SecurityWebFilterChain
import reactor.core.publisher.Mono

@Configuration
@EnableWebFluxSecurity
class SecurityConfig(
    var userDetailsService: ReactiveUserDetailsService,
    val securityContextRepository: SecurityContextRepository
) {

    @Bean
    fun filterChain(http: ServerHttpSecurity): SecurityWebFilterChain {
        return http
            .securityContextRepository(securityContextRepository)
            .exceptionHandling {
                it.authenticationEntryPoint { swe, _ ->
                    Mono.fromRunnable { swe.response.setStatusCode(HttpStatus.UNAUTHORIZED) }
                }
                it.accessDeniedHandler { swe, _ ->
                    Mono.fromRunnable { swe.response.setStatusCode(HttpStatus.FORBIDDEN) }
                }
            }
            .authorizeExchange {
                it.pathMatchers(HttpMethod.POST, "/authenticate", "/signup").permitAll()
                it.pathMatchers(HttpMethod.GET, "/gift-certificates/**", "/tags/**").permitAll()
                it.pathMatchers(HttpMethod.POST, "/gift-certificates/**", "/tags/**").hasRole(ADMIN)
                it.pathMatchers(HttpMethod.PATCH, "/gift-certificates/**", "/tags/**").hasRole(ADMIN)
                it.pathMatchers(HttpMethod.DELETE).hasRole(ADMIN)
                it.pathMatchers("/orders/**").hasRole(USER)
                it.pathMatchers("/users/**").hasRole(ADMIN)
            }
            .formLogin { it.disable() }
            .logout { it.disable() }
            .csrf { it.disable() }
            .httpBasic { it.disable() }
            .build()
    }

    @Bean
    fun authenticationManager(): ReactiveAuthenticationManager {
        return UserDetailsRepositoryReactiveAuthenticationManager(userDetailsService)
            .apply { setPasswordEncoder(passwordEncoder) }
    }

    @get:Bean
    val passwordEncoder: PasswordEncoder
        get() = BCryptPasswordEncoder()

    companion object {
        val USER = Role.USER.name
        val ADMIN = Role.ADMIN.name
    }
}
