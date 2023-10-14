package com.epam.esm.service

import com.epam.esm.application.service.OrderService
import com.epam.esm.domain.Order
import com.epam.esm.infrastructure.mongo.repository.GiftCertificateRepository
import com.epam.esm.infrastucture.mongo.repository.OrderRepository
import com.epam.esm.infractucture.mongo.repository.UserRepository
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.test.StepVerifier
import java.math.BigDecimal
import java.time.LocalDateTime
import org.mockito.Mockito.`when` as whenever

@ExtendWith(MockitoExtension::class)
class OrderServiceImplTest {

    @Mock
    private lateinit var orderRepository: OrderRepository

    @Mock
    private lateinit var userRepository: UserRepository

    @Mock
    private lateinit var giftCertificateRepository: GiftCertificateRepository

    @InjectMocks
    private lateinit var orderService: OrderService

    @Test
    fun getAllByUserId() {
        val expected = listOf(FIRST_TEST_ORDER, SECOND_TEST_ORDER)

        whenever(orderRepository.findAllByUserId(TEST_ID, PAGE)).thenReturn(Flux.fromIterable(expected))

        val actual = orderService.getAllByUserId(TEST_ID, PAGE_NUM, PAGE_SIZE)

        StepVerifier.create(actual)
            .expectNextCount(expected.size.toLong())
            .verifyComplete()
    }

    @Test
    fun createShouldThrowEntityNotFoundException() {
        whenever(userRepository.findById(NOT_EXIST_ID)).thenReturn(Mono.empty())
        whenever(giftCertificateRepository.findById(TEST_ID)).thenReturn(Mono.empty())

        val actual = orderService.create(NOT_EXIST_ID, TEST_ID)

        StepVerifier.create(actual)
            .expectNext()
            .verifyError()
    }

    @Test
    fun getById() {
        whenever(orderRepository.findById(TEST_ID)).thenReturn(Mono.just(FIRST_TEST_ORDER))

        val actual = orderService.getById(TEST_ID)

        StepVerifier.create(actual)
            .expectNext(FIRST_TEST_ORDER)
            .verifyComplete()
    }

    @Test
    fun getByIdShouldThrowEntityNotFoundException() {
        whenever(orderRepository.findById(NOT_EXIST_ID)).thenReturn(Mono.empty())

        val actual = orderService.getById(NOT_EXIST_ID)

        StepVerifier.create(actual)
            .expectNext()
            .verifyError()
    }

    companion object {
        const val TEST_ID: String = "1"

        const val NOT_EXIST_ID: String = "100"

        val FIRST_TEST_ORDER = Order(
            null,
            BigDecimal.ONE,
            LocalDateTime.now(),
            "1",
            "1"
        )

        val SECOND_TEST_ORDER = Order(
            null,
            BigDecimal.ONE,
            LocalDateTime.now(),
            "2",
            "2"
        )

        val PAGE: Pageable = PageRequest.of(0, 25)

        const val PAGE_NUM = 0

        const val PAGE_SIZE = 25
    }
}
