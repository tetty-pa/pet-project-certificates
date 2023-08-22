package com.epam.esm.repository

import com.epam.esm.model.entity.Order
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository

interface OrderRepository : JpaRepository<Order, Long> {
    /**
     * Gets list of Orderby user ID.
     *
     * @param userId ID of user
     * @param pageable object with pagination info(page number, page size)
     * @return list of orders
     */
    fun getAllByUserId(userId: Long, pageable: Pageable): List<Order>
}
