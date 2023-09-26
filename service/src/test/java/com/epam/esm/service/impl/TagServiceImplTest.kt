package com.epam.esm.service.impl

import com.epam.esm.repository.TagRepository
import com.epam.esm.repository.UserRepository
import com.epam.esm.service.impl.util.Constants.FIRST_TEST_TAG
import com.epam.esm.service.impl.util.Constants.NOT_EXIST_ID
import com.epam.esm.service.impl.util.Constants.PAGE
import com.epam.esm.service.impl.util.Constants.PAGE_NUM
import com.epam.esm.service.impl.util.Constants.PAGE_SIZE
import com.epam.esm.service.impl.util.Constants.SECOND_TEST_TAG
import com.epam.esm.service.impl.util.Constants.TAG_TO_CREATE
import com.epam.esm.service.impl.util.Constants.TEST_ID
import com.epam.esm.service.impl.util.Constants.THIRD_TEST_TAG
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.dao.DuplicateKeyException
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.test.StepVerifier
import org.mockito.Mockito.`when` as whenever

@ExtendWith(MockitoExtension::class)
class TagServiceImplTest {

    @Mock
    private lateinit var tagRepository: TagRepository

    @InjectMocks
    private lateinit var tagService: TagServiceImpl

    @Test
    fun getAll() {
        val expected = listOf(FIRST_TEST_TAG, SECOND_TEST_TAG, THIRD_TEST_TAG)
        whenever(tagRepository.findAll(PAGE)).thenReturn(Flux.fromIterable(expected))

        val actual = tagService.getAll(PAGE_NUM, PAGE_SIZE)

        StepVerifier.create(actual)
            .expectNextCount(expected.size.toLong())
            .verifyComplete()
    }

    @Test
    fun create() {
        whenever(tagRepository.save(TAG_TO_CREATE)).thenReturn(Mono.just(TAG_TO_CREATE))

        val actual = tagService.create(TAG_TO_CREATE)

        StepVerifier.create(actual)
            .expectNext(TAG_TO_CREATE)
            .verifyComplete()
    }

    @Test
    fun createShouldThrowDuplicateEntityException() {
        whenever(tagRepository.save(FIRST_TEST_TAG)).thenReturn(Mono.error(DuplicateKeyException("")))

        val actual = tagService.create(FIRST_TEST_TAG)

        StepVerifier.create(actual)
            .expectNext()
            .verifyError()
    }

    @Test
    fun getById() {
        whenever(tagRepository.findById(TEST_ID)).thenReturn(Mono.just(FIRST_TEST_TAG))
        val actual = tagService.getById(TEST_ID)

        StepVerifier.create(actual)
            .expectNext(FIRST_TEST_TAG)
            .verifyComplete()
    }

    @Test
    fun getByIdShouldThrowEntityNotFoundException() {
        whenever(tagRepository.findById(NOT_EXIST_ID)).thenReturn(Mono.empty())
        val actual = tagService.getById(NOT_EXIST_ID)

        StepVerifier.create(actual)
            .expectNext()
            .verifyError()
    }

    @Test
    fun deleteByIdShouldThrowEntityNotFoundException() {
        whenever(tagRepository.findById(NOT_EXIST_ID)).thenReturn(Mono.empty())

        val actual = tagService.deleteById(NOT_EXIST_ID)

        StepVerifier.create(actual)
            .expectNext()
            .verifyError()
    }
}
