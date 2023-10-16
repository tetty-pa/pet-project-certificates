package com.epam.esm.application.security

import com.epam.esm.application.security.util.JwtUtil
import org.springframework.security.core.context.SecurityContext
import org.springframework.security.core.context.SecurityContextImpl
import org.springframework.security.web.server.context.ServerSecurityContextRepository
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono

@Component
class SecurityContextRepository(
    private val jwtUtil: JwtUtil
) : ServerSecurityContextRepository {

    override fun save(exchange: ServerWebExchange, context: SecurityContext): Mono<Void> {
        throw UnsupportedOperationException("Not supported yet.")
    }

    override fun load(exchange: ServerWebExchange): Mono<SecurityContext> {
        return Mono.fromSupplier { jwtUtil.resolveToken(exchange.request) }
            .filter { it != null && jwtUtil.validateToken(it) }
            .flatMap { jwtUtil.getAuthentication(it!!) }
            .map { SecurityContextImpl(it) }
    }
}
