package com.epam.esm.service

import com.epam.esm.model.entity.Order
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface OrderService {
    /**
     * Gets list of from database by user ID.
     *
     * @param userId ID of user
     * @param page   page number for pagination
     * @param size   page size for pagination
     * @return list of orders Flux<Order>
     */
    fun getAllByUserId(userId: String, page: Int, size: Int): Flux<Order>
    /**
     * Creates an Order.
     *
     * @param userId user`s ID
     * @param certificateId certificate`s ID
     * @ return created Mono<Order>
     */
    fun create(userId: String, certificateId: String): Mono<Order>

    /**
     * Gets  from database by  ID.
     *
     * @param orderId ID of user
     * @return Mono<Order>
     */
    fun getById(orderId: String): Mono<Order>
}
