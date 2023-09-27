package com.epam.esm.web.security.util

import com.epam.esm.exception.InvalidJwtException
import com.epam.esm.model.entity.Role
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jws
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.http.server.reactive.ServerHttpRequest
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.ReactiveUserDetailsService
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import java.util.Date
import java.util.function.Function

@Component
class JwtUtil(private val personUserDetailsService: ReactiveUserDetailsService) {
    fun exactUsername(token: String): String =
        extractClaim(token) { obj: Claims -> obj.subject }

    private fun <T> extractClaim(token: String, claimsResolver: Function<Claims, T>): T {
        val claims = extractAllClaims(token)
        return claimsResolver.apply(claims)
    }

    private fun extractAllClaims(token: String): Claims =
        Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token).body

    fun generateToken(userName: String, role: String): String {
        val claims: Claims = Jwts.claims().setSubject(userName)
        claims["roles"] = Role.valueOf(role)
        return createToken(claims, userName)
    }

    private fun createToken(claims: Map<String, Any>, subject: String): String =
        Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(Date(System.currentTimeMillis()))
            .setExpiration(Date(System.currentTimeMillis() + TIME))
            .signWith(SignatureAlgorithm.HS256, SECRET).compact()

    fun validateToken(token: String): Boolean {
        return runCatching {
            val claims: Jws<Claims> = Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token)
            claims.body.expiration.after(Date())
        }.getOrElse { throw InvalidJwtException("Expired or invalid JWT token: $it") }
    }

    fun resolveToken(serverRequest: ServerHttpRequest): String? {
        val token = serverRequest.headers.getFirst("Authorization")
        if (token != null && token.startsWith("Bearer ")) {
            return token.substring(startIndex = 7)
        }
        return null
    }

    fun getAuthentication(token: String): Mono<Authentication> {
        return personUserDetailsService
            .findByUsername(exactUsername(token))
            .map { UsernamePasswordAuthenticationToken(it, "", it.authorities) }
    }

    companion object {
        private const val TIME = 1000 * 60 * 60 * 10
        private const val SECRET = "secret"
    }
}
