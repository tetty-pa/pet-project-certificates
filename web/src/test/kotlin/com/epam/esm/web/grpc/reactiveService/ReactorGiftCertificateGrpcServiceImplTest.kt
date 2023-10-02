package com.epam.esm.web.grpc.reactiveService

import assertk.assertThat
import assertk.assertions.isEqualTo
import com.epam.esm.GiftCertificateOuterClass.CreateGiftCertificateRequest
import com.epam.esm.GiftCertificateOuterClass.CreateGiftCertificateResponse
import com.epam.esm.GiftCertificateOuterClass.DeleteByIdGiftCertificateRequest
import com.epam.esm.GiftCertificateOuterClass.DeleteByIdGiftCertificateResponse
import com.epam.esm.GiftCertificateOuterClass.GetAllGiftCertificateRequest
import com.epam.esm.GiftCertificateOuterClass.GetAllGiftCertificateResponse
import com.epam.esm.GiftCertificateOuterClass.GetByIdGiftCertificateRequest
import com.epam.esm.GiftCertificateOuterClass.GetByIdGiftCertificateResponse
import com.epam.esm.GiftCertificateOuterClass.UpdateGiftCertificateRequest
import com.epam.esm.GiftCertificateOuterClass.UpdateGiftCertificateResponse
import com.epam.esm.grpcService.ReactorGiftCertificateServiceGrpc
import com.epam.esm.model.entity.GiftCertificate
import com.epam.esm.repository.GiftCertificateRepository
import com.epam.esm.service.GiftCertificateService
import com.epam.esm.web.converter.GiftCertificateConverter
import io.grpc.ManagedChannel
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.domain.PageRequest
import reactor.core.publisher.Mono
import reactor.test.StepVerifier
import java.math.BigDecimal
import java.time.LocalDateTime

@SpringBootTest
class ReactorGiftCertificateGrpcServiceImplTest {
    @Autowired
    lateinit var channel: ManagedChannel

    @Autowired
    private lateinit var giftCertificateConverter: GiftCertificateConverter

    @Autowired
    private lateinit var giftCertificateService: GiftCertificateService

    @Autowired
    private lateinit var giftCertificateRepository: GiftCertificateRepository


    private lateinit var stub: ReactorGiftCertificateServiceGrpc.ReactorGiftCertificateServiceStub

    @BeforeEach
    fun startClient() {
        stub = ReactorGiftCertificateServiceGrpc.newReactorStub(channel)
    }

    @AfterEach
    fun cleanDatabase() {
        val giftCertificate =
            giftCertificateRepository.findByName(TEST_GIFT_CERTIFICATE.name).block()
        giftCertificateRepository.deleteById(giftCertificate?.id ?: "").block()
    }

    @Test
    fun getAll() {
        val expected =
            GetAllGiftCertificateResponse.newBuilder().addAllGiftCertificates(
                giftCertificateService.getAll(page = PAGE.pageNumber, size = PAGE.pageSize).map {
                    giftCertificateConverter.entityToProto(it)
                }.collectList().block()
            ).build()

        val request =
            Mono.just(
                GetAllGiftCertificateRequest.newBuilder()
                    .setPage(PAGE.pageNumber).setSize(PAGE.pageSize).build()
            )

        val actual = stub.getAll(request)

        StepVerifier.create(actual)
            .expectNext(expected)
            .verifyComplete()
    }

    @Test
    fun getById() {
        val addedGiftCertificate =
            giftCertificateService.create(TEST_GIFT_CERTIFICATE).block()!!

        val expected =
            GetByIdGiftCertificateResponse
                .newBuilder()
                .setGiftCertificate(giftCertificateConverter.entityToProto(TEST_GIFT_CERTIFICATE))
                .build()

        val request =
            Mono.just(
                GetByIdGiftCertificateRequest
                    .newBuilder()
                    .setGiftCertificateId(addedGiftCertificate.id)
                    .build()
            )

        val actual = stub.getById(request)

        StepVerifier.create(actual)
            .assertNext {
                assertThat( expected.giftCertificate.name).isEqualTo(it.giftCertificate.name)
            }
            .verifyComplete()
    }

    @Test
    fun create() {
        val expected =
            CreateGiftCertificateResponse
                .newBuilder()
                .setGiftCertificate(giftCertificateConverter.entityToProto(TEST_GIFT_CERTIFICATE))
                .build()

        val request =
            Mono.just(
                CreateGiftCertificateRequest
                    .newBuilder()
                    .setGiftCertificate(giftCertificateConverter.entityToProto(TEST_GIFT_CERTIFICATE))
                    .build()
            )

        val actual = stub.create(request)

        StepVerifier.create(actual)
            .assertNext {
                assertThat( expected.giftCertificate.name).isEqualTo(it.giftCertificate.name)
            }
            .verifyComplete()
    }

    @Test
    fun update() {
        val addedGiftCertificate = giftCertificateService.create(TEST_GIFT_CERTIFICATE_CHANGED).block()!!
        TEST_GIFT_CERTIFICATE.id = addedGiftCertificate.id

        val certificate = giftCertificateConverter
            .entityToProto(TEST_GIFT_CERTIFICATE)

        val expected =
            UpdateGiftCertificateResponse
                .newBuilder()
                .setGiftCertificate(certificate)
                .build()

        val request =
            Mono.just(
                UpdateGiftCertificateRequest.newBuilder()
                    .setGiftCertificate(certificate)
                    .setId(addedGiftCertificate.id)
                    .build()
            )
        val actual =
            stub.update(request)

        StepVerifier.create(actual)
            .assertNext {
                assertThat( expected.giftCertificate.name).isEqualTo(it.giftCertificate.name)
            }
            .verifyComplete()
    }

    @Test
    fun deleteById() {
        val addedGiftCertificate = giftCertificateService.create(TEST_GIFT_CERTIFICATE).block()!!

        val request =
            Mono.just(
                DeleteByIdGiftCertificateRequest.newBuilder()
                    .setGiftCertificateId(addedGiftCertificate.id).build()
            )

        val actual = stub.deleteById(request)

        StepVerifier.create(actual)
            .expectNext(DeleteByIdGiftCertificateResponse.getDefaultInstance())
            .verifyComplete()
    }

    companion object {
        val TEST_GIFT_CERTIFICATE = GiftCertificate(
            "1", "1", BigDecimal(1),
            LocalDateTime.now(),
            LocalDateTime.now(), 1, mutableListOf()
        )
        val TEST_GIFT_CERTIFICATE_CHANGED = GiftCertificate(
            "new name", "1", BigDecimal(1),
            LocalDateTime.now(),
            LocalDateTime.now(), 1, mutableListOf()
        )
        val PAGE: PageRequest = PageRequest.of(1, 2)
    }
}
