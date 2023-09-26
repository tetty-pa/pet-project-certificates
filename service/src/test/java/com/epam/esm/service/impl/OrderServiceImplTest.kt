package com.epam.esm.service.impl

import com.epam.esm.repository.GiftCertificateRepository
import com.epam.esm.repository.OrderRepository
import com.epam.esm.repository.UserRepository
import com.epam.esm.service.impl.util.Constants.FIRST_TEST_ORDER
import com.epam.esm.service.impl.util.Constants.NOT_EXIST_ID
import com.epam.esm.service.impl.util.Constants.PAGE
import com.epam.esm.service.impl.util.Constants.PAGE_NUM
import com.epam.esm.service.impl.util.Constants.PAGE_SIZE
import com.epam.esm.service.impl.util.Constants.SECOND_TEST_ORDER
import com.epam.esm.service.impl.util.Constants.TEST_ID
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.test.StepVerifier
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
    private lateinit var orderService: OrderServiceImpl

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
}
