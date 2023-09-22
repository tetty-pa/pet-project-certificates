package com.epam.esm.service

import com.epam.esm.model.entity.Order
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface OrderService {
    fun getAllByUserId(userId: String, page: Int, size: Int): Flux<Order>

    fun create(user: String, certificate: String): Mono<Order>

    fun getById(orderId: String): Mono<Order>
}
