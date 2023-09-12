package com.epam.esm.repository.impl

import com.epam.esm.model.entity.Order
import com.epam.esm.repository.OrderRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.support.PageableExecutionUtils
import org.springframework.stereotype.Repository

@Repository
class OrderRepositoryImpl(private val mongoTemplate: MongoTemplate) : OrderRepository {
    override fun findAllByUserId(userId: String, page: Pageable): Page<Order> {
        val query = Query().with(page).addCriteria(Criteria.where("user._id").`is`(userId))
        return PageableExecutionUtils.getPage(
            mongoTemplate.find(query, Order::class.java),
            page
        ) { mongoTemplate.count(query, Order::class.java) }
    }

    override fun findById(id: String): Order? =
        mongoTemplate.findById(id, Order::class.java)

    override fun save(order: Order): Order =
        mongoTemplate.save(order)
}
