/*
package com.epam.esm.web.filter

import com.epam.esm.exception.InvalidJwtException
import com.epam.esm.service.security.PersonUserDetailsService
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class JwtRequestFilter(private val personUserDetailsService: PersonUserDetailsService,
    private val jwtUtil: JwtUtil): OncePerRequestFilter(){


    override fun doFilterInternal(
        httpServletRequest: HttpServletRequest,
        httpServletResponse: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val header = httpServletRequest.getHeader("Authorization")

        var userName: String? = null
        var jwt: String? = null

        if (header != null && header.startsWith("Bearer ")) {
            jwt = header.substring(7)
            userName = jwtUtil.exactUsername(jwt)
        }
        if (userName != null && SecurityContextHolder.getContext().authentication == null) {
            val userDetails = personUserDetailsService.loadUserByUsername(userName)
            if (jwtUtil.validateToken(jwt, userDetails)) {
                val usernamePasswordAuthenticationToken =
                    UsernamePasswordAuthenticationToken(userDetails, null, userDetails.authorities)
                usernamePasswordAuthenticationToken.details =
                    WebAuthenticationDetailsSource().buildDetails(httpServletRequest)
                SecurityContextHolder.getContext().authentication = usernamePasswordAuthenticationToken
            } else throw InvalidJwtException("JWT token is expired or invalid")
        }
        filterChain.doFilter(httpServletRequest, httpServletResponse)
    }
}*/
