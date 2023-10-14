package com.epam.esm.service

import com.epam.esm.application.service.UserService
import com.epam.esm.domain.Role
import com.epam.esm.domain.User
import com.epam.esm.infractucture.mongo.repository.UserRepository
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.dao.DuplicateKeyException
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
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
    private lateinit var userService: UserService

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

    companion object {
        const val TEST_ID: String = "1"

        val FIRST_TEST_USER = User("1", "1", "1", "", Role.USER)

        val SECOND_TEST_USER = User("2", "2", "2", "", Role.USER)

        val USER_TO_CREATE = User("1", "1", "1", "", Role.USER)

        val PAGE: Pageable = PageRequest.of(0, 25)

        const val PAGE_NUM = 0

        const val PAGE_SIZE = 25
    }
}
