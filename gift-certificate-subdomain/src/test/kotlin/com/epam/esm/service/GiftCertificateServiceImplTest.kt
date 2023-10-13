package com.epam.esm.service

import com.epam.esm.GiftCertificateOuterClass
import com.epam.esm.KafkaTopic
import com.epam.esm.application.proto.converter.DateConverter
import com.epam.esm.application.proto.converter.GiftCertificateConverter
import com.epam.esm.application.service.GiftCertificateService
import com.epam.esm.domain.GiftCertificate
import com.epam.esm.infrastructure.persistence.repository.mongo.GiftCertificateRepository
import com.google.protobuf.GeneratedMessageV3
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.dao.DuplicateKeyException
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.kafka.core.reactive.ReactiveKafkaProducerTemplate
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.test.StepVerifier
import java.math.BigDecimal
import java.sql.Timestamp
import java.time.ZoneOffset
import org.mockito.Mockito.`when` as whenever

@ExtendWith(MockitoExtension::class)
class GiftCertificateServiceImplTest {

    private lateinit var dateConverter: DateConverter

    @Mock
    private lateinit var giftCertificateRepository: GiftCertificateRepository

    @Mock
    private lateinit var reactiveKafkaProducerTemplate: ReactiveKafkaProducerTemplate<String, GeneratedMessageV3>

    @Mock
    private lateinit var giftCertificateConverter: GiftCertificateConverter

    @InjectMocks
    private lateinit var giftCertificateService: GiftCertificateService

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
        whenever(giftCertificateConverter.entityToProto(GIFT_CERTIFICATE_TO_CREATE)).thenReturn(
            GIFT_CERTIFICATE_TO_CREATE_PROTO
        )
        whenever(
            reactiveKafkaProducerTemplate.send(
                KafkaTopic.ADD_GIFT_CERTIFICATE_TOPIC,
                GiftCertificateOuterClass.StreamAllGiftCertificatesResponse.newBuilder()
                    .setNewGiftCertificate(giftCertificateConverter.entityToProto(GIFT_CERTIFICATE_TO_CREATE))
                    .build()
            )
        ).thenReturn(Mono.empty())

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

    companion object {
        const val TEST_ID: String = "1"

        const val NOT_EXIST_ID: String = "100"

        val FIRST_TEST_GIFT_CERTIFICATE = GiftCertificate(null,
            "1", "1", BigDecimal(1),
            Timestamp.valueOf("2023-01-04 12:07:19").toLocalDateTime(),
            Timestamp.valueOf("2023-01-04 12:07:19").toLocalDateTime(), 1, mutableListOf()
        )

        val SECOND_TEST_GIFT_CERTIFICATE = GiftCertificate(null,
            "2", "2", BigDecimal(2),
            Timestamp.valueOf("2023-01-04 12:07:19").toLocalDateTime(),
            Timestamp.valueOf("2023-01-04 12:07:19").toLocalDateTime(), 2, mutableListOf()
        )

        val THIRD_TEST_GIFT_CERTIFICATE = GiftCertificate(null,
            "3", "3", BigDecimal(3),
            Timestamp.valueOf("2023-01-04 12:07:19").toLocalDateTime(),
            Timestamp.valueOf("2023-01-04 12:07:19").toLocalDateTime(), 3, mutableListOf()
        )

        val GIFT_CERTIFICATE_TO_CREATE = GiftCertificate(null,
            "1", "certificate new", BigDecimal("1.10"),
            Timestamp.valueOf("2023-01-04 12:07:19").toLocalDateTime(),
            Timestamp.valueOf("2023-01-04 12:07:19").toLocalDateTime(), 1, mutableListOf()
        )
        val GIFT_CERTIFICATE_TO_CREATE_PROTO = GiftCertificateOuterClass.GiftCertificate.newBuilder().apply {
            name = "1"
            description = "certificate new"
            duration = 1
            price = BigDecimal("1.10").toDouble()
            createDate = com.google.protobuf.Timestamp.newBuilder()
                .setSeconds(GIFT_CERTIFICATE_TO_CREATE.createDate.toInstant(ZoneOffset.UTC).epochSecond)
                .setNanos(GIFT_CERTIFICATE_TO_CREATE.createDate.nano)
                .build()
            lastUpdatedDate = com.google.protobuf.Timestamp.newBuilder()
                .setSeconds(GIFT_CERTIFICATE_TO_CREATE.lastUpdatedDate.toInstant(ZoneOffset.UTC).epochSecond)
                .setNanos(GIFT_CERTIFICATE_TO_CREATE.lastUpdatedDate.nano)
                .build()
            addAllTags(mutableListOf())
        }.build()

        val PAGE: Pageable = PageRequest.of(0, 25)

        const val PAGE_NUM = 0

        const val PAGE_SIZE = 25
    }
}
