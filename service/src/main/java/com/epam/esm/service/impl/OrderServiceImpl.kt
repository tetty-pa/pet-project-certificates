package com.epam.esm.service.impl

import com.epam.esm.exception.EntityNotFoundException
import com.epam.esm.model.entity.Order
import com.epam.esm.repository.GiftCertificateRepository
import com.epam.esm.repository.OrderRepository
import com.epam.esm.repository.UserRepository
import com.epam.esm.service.OrderService
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class OrderServiceImpl(
    private val orderRepository: OrderRepository,
    private val userRepository: UserRepository,
    private val giftCertificateRepository: GiftCertificateRepository
) : OrderService {
    override fun getAllByUserId(userId: String, page: Int, size: Int): List<Order> {
        userRepository.findById(userId) ?: throw EntityNotFoundException("user.notfoundById")

        val pageRequest: Pageable = PageRequest.of(page, size)
        return orderRepository.findAllByUserId(userId, pageRequest).content
    }

    override fun create(userId: String, certificateId: String): Order {
        val order = Order()

        val user = userRepository.findById(userId) ?: throw EntityNotFoundException("user.notfoundById")
        order.userId = user.id

        val giftCertificate = giftCertificateRepository.findById(certificateId)
            ?: throw EntityNotFoundException("gift-certificate.notfoundById")
        order.giftCertificateId = giftCertificate.id

        order.cost = giftCertificate.price
        return orderRepository.save(order)
    }

    override fun getById(orderId: String): Order =
        orderRepository.findById(orderId) ?: throw EntityNotFoundException("order.notfoundById")
}
