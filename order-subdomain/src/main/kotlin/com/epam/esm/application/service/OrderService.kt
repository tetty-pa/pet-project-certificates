package com.epam.esm.application.service

import com.epam.esm.domain.Order
import com.epam.esm.exception.EntityNotFoundException
import com.epam.esm.infrastructure.persistence.repository.mongo.GiftCertificateRepository
import com.epam.esm.repository.impl.OrderRepository
import com.epam.esm.repository.impl.UserRepository
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.time.LocalDateTime

@Service
class OrderService(
    private val orderRepository: OrderRepository,
    private val userRepository: UserRepository,
    private val giftCertificateRepository: GiftCertificateRepository
) : OrderServiceInPort {

    override fun getAllByUserId(userId: String, page: Int, size: Int): Flux<Order> {
        val pageRequest: Pageable = PageRequest.of(page, size)
        return orderRepository.findAllByUserId(userId, pageRequest)
    }

    override fun create(user: String, certificate: String): Mono<Order> {
        return Mono.zip(
            userRepository.findById(user),
            giftCertificateRepository.findById(certificate)
        )
            .switchIfEmpty(Mono.error(EntityNotFoundException("")))
            .flatMap {
                val order = Order(null)
                order.apply {
                    userId = it.t1.id!!
                    giftCertificateId = it.t2.id!!
                    cost = it.t2.price
                    orderDate = LocalDateTime.now()
                }
                orderRepository.save(order)
            }
    }

    override fun getById(orderId: String): Mono<Order> =
        orderRepository.findById(orderId)
            .switchIfEmpty(Mono.error(EntityNotFoundException("order.notfoundById")))
}
