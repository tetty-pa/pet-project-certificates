package com.epam.esm.web.security

import com.epam.esm.model.entity.Role
import com.epam.esm.service.security.PersonUserDetailsService
import com.epam.esm.web.exception.RestResponseEntityExceptionHandler
import com.epam.esm.web.filter.JwtRequestFilter
import com.epam.esm.web.filter.ServletJsonResponseSender
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
@EnableWebSecurity
class SecurityConfig(
    var userDetailsService: PersonUserDetailsService,
    var jwtRequestFilter: JwtRequestFilter,
    var handler: RestResponseEntityExceptionHandler,
    var jsonResponseSender: ServletJsonResponseSender
) {
    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http.authorizeHttpRequests {
            it.requestMatchers(HttpMethod.POST, "/authenticate", "/signup").permitAll()
                .requestMatchers(HttpMethod.GET, "/gift-certificates/**", "/tags/**").permitAll()
                .requestMatchers(HttpMethod.POST, "/gift-certificates/**", "/tags/**").hasRole(ADMIN)
                .requestMatchers(HttpMethod.PATCH, "/gift-certificates/**", "/tags/**").hasRole(ADMIN)
                .requestMatchers(HttpMethod.DELETE).hasRole(ADMIN)
                .requestMatchers("/orders/**").hasRole(USER)
        }
        http.authenticationManager(authenticationManager(http))
        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter::class.java)
        http.cors {}
        http.sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
        http.csrf { it.disable() }
        http.httpBasic { it.disable() }
        http.exceptionHandling {
            it.authenticationEntryPoint { request, response, ex ->
                handleNoJwt(request, response)
            }
        }.formLogin {}
        return http.build()
    }

    @Bean
    fun authenticationManager(http: HttpSecurity): AuthenticationManager {
        val authenticationManagerBuilder = http.getSharedObject(
            AuthenticationManagerBuilder::class.java
        )
        authenticationManagerBuilder.userDetailsService(userDetailsService)
        return authenticationManagerBuilder.build()
    }

    private fun handleNoJwt(request: HttpServletRequest, response: HttpServletResponse) {
        val locale = request.locale
        val responseObject = handler.buildNoJwtResponseObject(locale)
        jsonResponseSender.send(response, responseObject)
    }

    @get:Bean
    val passwordEncoder: PasswordEncoder
        get() = BCryptPasswordEncoder()

    companion object {
        val USER = Role.RoleType.USER.name
        val ADMIN = Role.RoleType.ADMIN.name
    }
}
