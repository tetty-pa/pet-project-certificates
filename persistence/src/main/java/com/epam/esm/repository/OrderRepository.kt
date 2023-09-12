package com.epam.esm.repository

import com.epam.esm.model.entity.Order
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface OrderRepository {
    fun findAllByUserId(userId: String, page: Pageable): Page<Order>

    fun findById(id: String): Order?

    fun save(order: Order): Order
}
