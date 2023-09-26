package com.epam.esm.service.impl

import com.epam.esm.repository.GiftCertificateRepository
import com.epam.esm.service.impl.util.Constants.FIRST_TEST_GIFT_CERTIFICATE
import com.epam.esm.service.impl.util.Constants.GIFT_CERTIFICATE_TO_CREATE
import com.epam.esm.service.impl.util.Constants.NOT_EXIST_ID
import com.epam.esm.service.impl.util.Constants.PAGE
import com.epam.esm.service.impl.util.Constants.PAGE_NUM
import com.epam.esm.service.impl.util.Constants.PAGE_SIZE
import com.epam.esm.service.impl.util.Constants.SECOND_TEST_GIFT_CERTIFICATE
import com.epam.esm.service.impl.util.Constants.TEST_ID
import com.epam.esm.service.impl.util.Constants.THIRD_TEST_GIFT_CERTIFICATE
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
class GiftCertificateServiceImplTest {

    @Mock
    private lateinit var giftCertificateRepository: GiftCertificateRepository

    @InjectMocks
    private lateinit var giftCertificateService: GiftCertificateServiceImpl

    @Test
    fun getAll() {
        val expected = listOf(FIRST_TEST_GIFT_CERTIFICATE, SECOND_TEST_GIFT_CERTIFICATE, THIRD_TEST_GIFT_CERTIFICATE)
        whenever(giftCertificateRepository.findAll(PAGE)).thenReturn(Flux.fromIterable(expected))

        val actual = giftCertificateService.getAll(PAGE_NUM, PAGE_SIZE)

        StepVerifier.create(actual)
            .expectNextCount(expected.size.toLong())
            .verifyComplete()
    }

    @Test
    fun getById() {
        whenever(giftCertificateRepository.findById(TEST_ID)).thenReturn(Mono.just(FIRST_TEST_GIFT_CERTIFICATE))

        val actual = giftCertificateService.getById(TEST_ID)

        StepVerifier.create(actual)
            .expectNext(FIRST_TEST_GIFT_CERTIFICATE)
            .verifyComplete()
    }

    @Test
    fun getByIdShouldThrowEntityNotFoundException() {
        whenever(giftCertificateRepository.findById(NOT_EXIST_ID)).thenReturn(Mono.empty())

        val actual = giftCertificateService.getById(NOT_EXIST_ID)

        StepVerifier.create(actual)
            .expectNext()
            .verifyError()
    }

    @Test
    fun create() {
        whenever(giftCertificateRepository.save(GIFT_CERTIFICATE_TO_CREATE)).thenReturn(
            Mono.just(GIFT_CERTIFICATE_TO_CREATE)
        )

        val actual = giftCertificateService.create(GIFT_CERTIFICATE_TO_CREATE)

        StepVerifier.create(actual)
            .expectNext(GIFT_CERTIFICATE_TO_CREATE)
            .verifyComplete()
    }

    @Test
    fun createShouldThrowDuplicateEntityException() {
        whenever(giftCertificateRepository.save(GIFT_CERTIFICATE_TO_CREATE)).thenReturn(
            Mono.error(DuplicateKeyException(""))
        )

        val actual = giftCertificateService.create(GIFT_CERTIFICATE_TO_CREATE)

        StepVerifier.create(actual)
            .expectNext()
            .verifyError()
    }

    @Test
    fun update() {
        GIFT_CERTIFICATE_TO_CREATE.id = "1"
        whenever(giftCertificateRepository.findById(TEST_ID)).thenReturn(
            Mono.just(GIFT_CERTIFICATE_TO_CREATE)
        )
        whenever(giftCertificateRepository.save(GIFT_CERTIFICATE_TO_CREATE)).thenReturn(
            Mono.just(GIFT_CERTIFICATE_TO_CREATE)
        )

        val actual = giftCertificateService.update(GIFT_CERTIFICATE_TO_CREATE)

        StepVerifier.create(actual)
            .expectNext(GIFT_CERTIFICATE_TO_CREATE)
            .verifyComplete()
    }

    @Test
    fun updateShouldThrowEntityNotFoundException() {
        GIFT_CERTIFICATE_TO_CREATE.id = "1"
        whenever(giftCertificateRepository.findById(TEST_ID)).thenReturn(
            Mono.empty()
        )

        val actual = giftCertificateService.update(GIFT_CERTIFICATE_TO_CREATE)

        StepVerifier.create(actual)
            .expectNext()
            .verifyError()
    }

    @Test
    fun deleteByIdShouldThrowEntityNotFoundException() {
        whenever(giftCertificateRepository.findById(NOT_EXIST_ID)).thenReturn(Mono.empty())

        val actual = giftCertificateService.deleteById(NOT_EXIST_ID)

        StepVerifier.create(actual)
            .expectNext()
            .verifyError()
    }
}
