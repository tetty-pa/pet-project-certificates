package com.epam.esm.repository.impl

import com.epam.esm.model.entity.Order
import com.epam.esm.repository.OrderRepository
import org.springframework.data.domain.Pageable
import org.springframework.data.mongodb.core.ReactiveMongoTemplate
import org.springframework.data.mongodb.core.query.Query
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Repository
class OrderRepositoryImpl(
    private val mongoTemplate: ReactiveMongoTemplate
) : OrderRepository {

    override fun findAllByUserId(userId: String, page: Pageable): Flux<Order> {
        val query = Query().with(page)
        return mongoTemplate.find(query, Order::class.java)
    }

    override fun findById(id: String): Mono<Order> =
        mongoTemplate.findById(id, Order::class.java)

    override fun save(order: Order): Mono<Order> =
        mongoTemplate.save(order)
}
