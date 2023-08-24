package com.epam.esm.service.impl

import com.epam.esm.exception.EntityNotFoundException
import com.epam.esm.repository.UserRepository
import com.epam.esm.service.impl.util.Constants.FIRST_TEST_USER
import com.epam.esm.service.impl.util.Constants.NOT_EXIST_ID
import com.epam.esm.service.impl.util.Constants.PAGE
import com.epam.esm.service.impl.util.Constants.PAGE_NUM
import com.epam.esm.service.impl.util.Constants.PAGE_SIZE
import com.epam.esm.service.impl.util.Constants.SECOND_TEST_USER
import com.epam.esm.service.impl.util.Constants.TEST_ID
import com.epam.esm.service.impl.util.Constants.USER_TO_CREATE
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.data.domain.PageImpl
import org.springframework.security.crypto.password.PasswordEncoder
import java.util.*
import org.mockito.Mockito.`when` as whenever

@ExtendWith(MockitoExtension::class)
class UserServiceImplTest {

    @Mock
    private lateinit var userRepository: UserRepository

    @Mock
    private lateinit var passwordEncoder: PasswordEncoder

    @InjectMocks
    private lateinit var userService: UserServiceImpl

    @Test
    fun getAll() {
        val expected = listOf(FIRST_TEST_USER, SECOND_TEST_USER)
        whenever(userRepository.findAll(PAGE)).thenReturn(PageImpl(expected))

        val actual = userService.getAll(PAGE_NUM, PAGE_SIZE)

        assertEquals(expected, actual)
    }

    @Test
    fun getById() {
        whenever(userRepository.findById(TEST_ID)).thenReturn(Optional.of(FIRST_TEST_USER))
        val actual = userService.getById(TEST_ID)
        assertEquals(FIRST_TEST_USER, actual)
    }

    @Test
    fun getByIdShouldThrowEntityNotFoundException() {
        whenever(userRepository.findById(NOT_EXIST_ID)).thenThrow(EntityNotFoundException(""))
        assertThrows<EntityNotFoundException> { userService.getById(NOT_EXIST_ID) }
    }

    @Test
    fun create() {
        whenever(userRepository.save(USER_TO_CREATE)).thenReturn(USER_TO_CREATE)
        whenever(passwordEncoder.encode(USER_TO_CREATE.password)).thenReturn(USER_TO_CREATE.password)

        val actual = userService.create(USER_TO_CREATE)
        assertEquals(USER_TO_CREATE, actual)
    }
}
