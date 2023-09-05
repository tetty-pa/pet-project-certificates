package com.epam.esm.service

import com.epam.esm.model.entity.Order

interface OrderService {
    /**
     * Gets list of {@link Order} from database by user ID.
     *
     * @param userId ID of user
     * @param page   page number for pagination
     * @param size   page size for pagination
     * @return list of orders
     */
    fun getAllByUserId(userId: String, page: Int, size: Int): List<Order>
    /**
    * Creates an Order.
    *
    * @param userId user`s ID
    * @param certificateId certificate`s ID
    * @ return Entity
    object
    */
    fun create(userId: String, certificateId: String): Order

    /**
     * Gets  {@link Order} from database by  ID.
     *
     * @param orderId ID of user
     * @return order
     */
    fun getById(orderId: String): Order
}
