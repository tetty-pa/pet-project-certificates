package com.epam.esm.service

import com.epam.esm.application.repository.TagRedisRepositoryOutPort
import com.epam.esm.application.repository.TagRepositoryOutPort
import com.epam.esm.application.service.TagService
import com.epam.esm.domain.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.dao.DuplicateKeyException
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.test.StepVerifier
import org.mockito.Mockito.`when` as whenever

@ExtendWith(MockitoExtension::class)
class TagServiceImplTest {

    @Mock
    private lateinit var tagRepositoryOutPort: TagRepositoryOutPort

    @Mock
    private lateinit var tagRedisRepositoryOutPort: TagRedisRepositoryOutPort

    @InjectMocks
    private lateinit var tagService: TagService

    @Test
    fun getAll() {
        val expected = listOf(FIRST_TEST_TAG, SECOND_TEST_TAG, THIRD_TEST_TAG)
        whenever(tagRepositoryOutPort.findAll(PAGE)).thenReturn(Flux.fromIterable(expected))

        val actual = tagService.getAll(PAGE_NUM, PAGE_SIZE)

        StepVerifier.create(actual)
            .expectNextCount(expected.size.toLong())
            .verifyComplete()
    }

    @Test
    fun create() {
        whenever(tagRepositoryOutPort.save(TAG_TO_CREATE)).thenReturn(Mono.just(TAG_TO_CREATE))

        val actual = tagService.create(TAG_TO_CREATE)

        StepVerifier.create(actual)
            .expectNext(TAG_TO_CREATE)
            .verifyComplete()
    }

    @Test
    fun createShouldThrowDuplicateEntityException() {
        whenever(tagRepositoryOutPort.save(FIRST_TEST_TAG)).thenReturn(Mono.error(DuplicateKeyException("")))

        val actual = tagService.create(FIRST_TEST_TAG)

        StepVerifier.create(actual)
            .expectNext()
            .verifyError()
    }

    @Test
    fun getById() {
        whenever(tagRepositoryOutPort.findById(TEST_ID)).thenReturn(Mono.just(FIRST_TEST_TAG))
        whenever(tagRedisRepositoryOutPort.findById(TEST_ID)).thenReturn(Mono.empty())
        whenever(tagRedisRepositoryOutPort.save(FIRST_TEST_TAG)).thenReturn(Mono.just(FIRST_TEST_TAG))

        val actual = tagService.getById(TEST_ID)

        StepVerifier.create(actual)
            .expectNext(FIRST_TEST_TAG)
            .verifyComplete()
    }

    @Test
    fun getByIdShouldThrowEntityNotFoundException() {
        whenever(tagRepositoryOutPort.findById(NOT_EXIST_ID)).thenReturn(Mono.empty())
        whenever(tagRedisRepositoryOutPort.findById(NOT_EXIST_ID)).thenReturn(Mono.empty())
        val actual = tagService.getById(NOT_EXIST_ID)

        StepVerifier.create(actual)
            .expectNext()
            .verifyError()
    }

    @Test
    fun deleteByIdShouldThrowEntityNotFoundException() {
        whenever(tagRepositoryOutPort.findById(NOT_EXIST_ID)).thenReturn(Mono.empty())
     val actual = tagService.deleteById(NOT_EXIST_ID)

        StepVerifier.create(actual)
            .expectNext()
            .verifyError()
    }

    companion object {
        const val TEST_ID: String = "1"

        const val NOT_EXIST_ID: String = "100"

        val FIRST_TEST_TAG = Tag(null, "1")

        val SECOND_TEST_TAG = Tag(null, "2")

        val THIRD_TEST_TAG = Tag(null, "3")

        val TAG_TO_CREATE = Tag(null, "new")

        val PAGE: Pageable = PageRequest.of(0, 25)

        const val PAGE_NUM = 0

        const val PAGE_SIZE = 25
    }
}
