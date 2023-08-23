package com.epam.esm.web.filter

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Component
import java.util.Date
import java.util.function.Function

@Component
class JwtUtil {
    val secret = "secret"
    fun exactUsername(token: String): String {
        return extractClaim(token) { obj: Claims -> obj.subject }
    }

    fun exactExpiration(token: String): Date {
        return extractClaim(token) { obj: Claims -> obj.expiration }
    }

    private fun <T> extractClaim(token: String, claimsResolver: Function<Claims, T>): T {
        val claims = extractAllClaims(token)
        return claimsResolver.apply(claims)
    }

    private fun extractAllClaims(token: String): Claims {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).body
    }

    private fun isTokenExpired(token: String): Boolean {
        return exactExpiration(token).before(Date())
    }

    fun generateToken(userDetails: UserDetails): String {
        val claims: Map<String, Any> = HashMap()
        return createToken(claims, userDetails.username)
    }

    private fun createToken(claims: Map<String, Any>, subject: String): String {
        return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(Date(System.currentTimeMillis()))
                .setExpiration(Date(System.currentTimeMillis() + TIME))
                .signWith(SignatureAlgorithm.HS256, secret).compact()
    }

    fun validateToken(token: String?, userDetails: UserDetails): Boolean {
        val username = token?.let { exactUsername(it) }
        return username == userDetails.username && !token?.let { isTokenExpired(it) }!!
    }

    companion object {
        private const val TIME = 1000 * 60 * 60 * 10
    }
}
