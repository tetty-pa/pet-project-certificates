package com.epam.esm.service.impl

import com.epam.esm.repository.UserRepository
import com.epam.esm.service.impl.util.Constants.FIRST_TEST_USER
import com.epam.esm.service.impl.util.Constants.PAGE
import com.epam.esm.service.impl.util.Constants.PAGE_NUM
import com.epam.esm.service.impl.util.Constants.PAGE_SIZE
import com.epam.esm.service.impl.util.Constants.SECOND_TEST_USER
import com.epam.esm.service.impl.util.Constants.TEST_ID
import com.epam.esm.service.impl.util.Constants.USER_TO_CREATE
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.dao.DuplicateKeyException
import org.springframework.security.crypto.password.PasswordEncoder
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.test.StepVerifier
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
        whenever(userRepository.findAll(PAGE)).thenReturn(Flux.fromIterable(expected))

        val actual = userService.getAll(PAGE_NUM, PAGE_SIZE)

        StepVerifier.create(actual)
            .expectNextCount(expected.size.toLong())
            .verifyComplete()
    }

    @Test
    fun getById() {
        whenever(userRepository.findById(TEST_ID)).thenReturn(Mono.just(FIRST_TEST_USER))

        val actual = userService.getById(TEST_ID)

        StepVerifier.create(actual)
            .expectNext(FIRST_TEST_USER)
            .verifyComplete()
    }

    @Test
    fun getByIdShouldThrowEntityNotFoundException() {
        whenever(userRepository.findById(TEST_ID)).thenReturn(Mono.empty())

        val actual = userService.getById(TEST_ID)

        StepVerifier.create(actual)
            .expectNext()
            .verifyError()
    }

    @Test
    fun create() {
        whenever(passwordEncoder.encode(USER_TO_CREATE.password)).thenReturn(USER_TO_CREATE.password)
        whenever(userRepository.save(USER_TO_CREATE)).thenReturn(Mono.just(USER_TO_CREATE))

        val actual = userService.create(USER_TO_CREATE)

        StepVerifier.create(actual)
            .expectNext(USER_TO_CREATE)
            .verifyComplete()
    }

    @Test
    fun createShouldThrowDuplicateEntityException() {
        whenever(passwordEncoder.encode(USER_TO_CREATE.password)).thenReturn(USER_TO_CREATE.password)
        whenever(userRepository.save(USER_TO_CREATE))
            .thenReturn(Mono.error(DuplicateKeyException("")))

        val actual = userService.create(USER_TO_CREATE)

        StepVerifier.create(actual)
            .expectNext()
            .verifyError()
    }
}
