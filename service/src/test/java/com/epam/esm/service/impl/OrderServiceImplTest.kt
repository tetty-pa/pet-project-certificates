package com.epam.esm.service.impl

import com.epam.esm.exception.EntityNotFoundException
import com.epam.esm.repository.GiftCertificateRepository
import com.epam.esm.repository.OrderRepository
import com.epam.esm.repository.UserRepository
import com.epam.esm.service.impl.util.Constants.FIRST_TEST_ORDER
import com.epam.esm.service.impl.util.Constants.FIRST_TEST_USER
import com.epam.esm.service.impl.util.Constants.NOT_EXIST_ID
import com.epam.esm.service.impl.util.Constants.PAGE
import com.epam.esm.service.impl.util.Constants.PAGE_NUM
import com.epam.esm.service.impl.util.Constants.PAGE_SIZE
import com.epam.esm.service.impl.util.Constants.SECOND_TEST_ORDER
import com.epam.esm.service.impl.util.Constants.TEST_ID
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import java.util.*
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
        whenever(orderRepository.getAllByUserId(TEST_ID, PAGE)).thenReturn(expected)
        whenever(userRepository.findById(TEST_ID)).thenReturn(Optional.of(FIRST_TEST_USER))

        val actual = orderService.getAllByUserId(TEST_ID, PAGE_NUM, PAGE_SIZE)

        assertEquals(expected, actual)
    }

    @Test
    fun getAllByUserIdShouldThrowEntityNotFoundException() {
        whenever(userRepository.findById(NOT_EXIST_ID)).thenThrow(EntityNotFoundException(""))
        assertThrows<EntityNotFoundException> { orderService.getAllByUserId(NOT_EXIST_ID, PAGE_NUM, PAGE_SIZE) }
    }

    @Test
    fun createShouldThrowEntityNotFoundException() {
        whenever(userRepository.findById(NOT_EXIST_ID)).thenThrow(EntityNotFoundException(""))
        assertThrows<EntityNotFoundException> { orderService.create(NOT_EXIST_ID, NOT_EXIST_ID) }
    }

    @Test
    fun getById() {
        whenever(orderRepository.findById(TEST_ID)).thenReturn(Optional.of(FIRST_TEST_ORDER))
        val actual = orderService.getById(TEST_ID)
        assertEquals(FIRST_TEST_ORDER, actual)
    }

    @Test
    fun getByIdShouldThrowEntityNotFoundException() {
        whenever(orderRepository.findById(NOT_EXIST_ID)).thenThrow(EntityNotFoundException(""))
        assertThrows<EntityNotFoundException> { orderService.getById(NOT_EXIST_ID) }
    }
}
