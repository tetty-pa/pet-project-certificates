package com.epam.esm.web.security

import com.epam.esm.model.entity.Role
import com.epam.esm.service.security.PersonUserDetailsService
import com.epam.esm.web.exception.RestResponseEntityExceptionHandler
/*
import com.epam.esm.web.filter.JwtRequestFilter
*/
import com.epam.esm.web.filter.ServletJsonResponseSender
import org.springframework.context.annotation.Bean
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import java.io.IOException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
class SecurityConfig(
    var userDetailsService: PersonUserDetailsService,
    /* var jwtRequestFilter: JwtRequestFilter,*/
    var handler: RestResponseEntityExceptionHandler,
    var jsonResponseSender: ServletJsonResponseSender
) : WebSecurityConfigurerAdapter() {
    @Throws(Exception::class)
    override fun configure(http: HttpSecurity) {
        http
            .csrf().disable()
            .httpBasic().disable() /*
                .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class)
*/
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and().authorizeRequests() /*

                .antMatchers(GET, "/gift-certificates/ **").permitAll()
                .antMatchers(POST, "/authenticate", "/signup").permitAll()

                .antMatchers("/orders/ **").hasAnyRole(USER, ADMIN)
                .antMatchers(GET, "/users/ **", "/tags/ **").hasRole( ADMIN)
*/
            .anyRequest().permitAll() /*.hasRole(ADMIN)
                */
            .and()
            .exceptionHandling() /*                .authenticationEntryPoint(
                        (request, response, ex) -> handleNoJwt(request, response)
                )*/
            .and().formLogin()
    }

    @Throws(IOException::class)
    private fun handleNoJwt(request: HttpServletRequest, response: HttpServletResponse) {
        val locale = request.locale
        val responseObject = handler.buildNoJwtResponseObject(locale)
        jsonResponseSender.send(response, responseObject)
    }

    @get:Bean
    val passwordEncoder: PasswordEncoder
        get() = BCryptPasswordEncoder()

    @Bean
    @Throws(Exception::class)
    override fun authenticationManager(): AuthenticationManager {
        return super.authenticationManager()
    }

    companion object {
        val USER = Role.RoleType.USER.name
        val ADMIN = Role.RoleType.ADMIN.name
    }
}
