package com.epam.esm.infrastucture.mongo.repository

import com.epam.esm.application.repository.OrderRepositoryOutPort
import com.epam.esm.domain.Order
import com.epam.esm.infrastucture.mongo.entity.OrderEntity
import com.epam.esm.infrastucture.mongo.mapper.OrderMapper
import org.springframework.data.domain.Pageable
import org.springframework.data.mongodb.core.ReactiveMongoTemplate
import org.springframework.data.mongodb.core.query.Query
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Repository
class OrderRepository(
    private val mongoTemplate: ReactiveMongoTemplate,
    private val orderMapper: OrderMapper
) : OrderRepositoryOutPort {

    override fun findAllByUserId(userId: String, page: Pageable): Flux<Order> {
        val query = Query().with(page)
        return mongoTemplate.find(query, OrderEntity::class.java)
            .map { orderMapper.mapToDomain(it) }
    }

    override fun findById(id: String): Mono<Order> =
        mongoTemplate.findById(id, OrderEntity::class.java)
            .map { orderMapper.mapToDomain(it) }


    override fun save(order: Order): Mono<Order> =
        mongoTemplate.save(orderMapper.mapToEntity(order))
            .map { orderMapper.mapToDomain(it) }

}
