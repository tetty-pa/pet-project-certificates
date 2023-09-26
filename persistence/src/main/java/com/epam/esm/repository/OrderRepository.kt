package com.epam.esm.repository

import com.epam.esm.model.entity.Order
import org.springframework.data.domain.Pageable
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface OrderRepository {
    fun findAllByUserId(userId: String, page: Pageable): Flux<Order>

    fun findById(id: String): Mono<Order>

    fun save(order: Order): Mono<Order>
}
