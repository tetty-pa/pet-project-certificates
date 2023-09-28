package com.epam.esm.web.grpc.reactiveService

import com.epam.esm.GiftCertificateOuterClass.CreateGiftCertificateRequest
import com.epam.esm.GiftCertificateOuterClass.DeleteByIdGiftCertificateRequest
import com.epam.esm.GiftCertificateOuterClass.GetAllGiftCertificateRequest
import com.epam.esm.GiftCertificateOuterClass.GetByIdGiftCertificateRequest
import com.epam.esm.GiftCertificateOuterClass.UpdateGiftCertificateRequest
import com.epam.esm.grpcService.ReactorGiftCertificateServiceGrpc
import com.epam.esm.model.entity.GiftCertificate
import com.epam.esm.repository.GiftCertificateRepository
import com.epam.esm.service.GiftCertificateService
import com.epam.esm.web.converter.GiftCertificateConverter
import io.grpc.ManagedChannel
import io.grpc.ManagedChannelBuilder
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.domain.PageRequest
import reactor.core.publisher.Mono
import java.math.BigDecimal
import java.time.LocalDateTime

@SpringBootTest
class ReactorGiftCertificateGrpcServiceImplTest(
    @Value("\${grpc.server.port}")
    var grpcPort: Int
) {
    @Autowired
    private lateinit var giftCertificateConverter: GiftCertificateConverter

    @Autowired
    private lateinit var giftCertificateService: GiftCertificateService

    @Autowired
    private lateinit var giftCertificateRepository: GiftCertificateRepository

    private lateinit var stub: ReactorGiftCertificateServiceGrpc.ReactorGiftCertificateServiceStub

    private lateinit var channel: ManagedChannel

    @BeforeEach
    fun startClient() {
        channel = ManagedChannelBuilder
            .forTarget("localhost:$grpcPort")
            .usePlaintext()
            .build()
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
            giftCertificateService.getAll(page = PAGE.pageNumber, size = PAGE.pageSize).map {
                giftCertificateConverter.entityToProto(it)
            }.collectList().block()

        val request =
            Mono.just(
                GetAllGiftCertificateRequest.newBuilder()
                    .setPage(1).setSize(2).build()
            )

        val actual = stub.getAll(request).block()

        assertThat(actual?.giftCertificatesList).isEqualTo(expected)
    }

    @Test
    fun getById() {
        val addedGiftCertificate =
            giftCertificateService.create(TEST_GIFT_CERTIFICATE).block()!!

        val expected =
            giftCertificateService
                .getById(addedGiftCertificate.id)
                .map {
                    giftCertificateConverter
                        .entityToProto(it)
                }.block()!!

        val request =
            Mono.just(
                GetByIdGiftCertificateRequest
                    .newBuilder()
                    .setGiftCertificateId(addedGiftCertificate.id)
                    .build()
            )

        val actual = stub.getById(request).block()

        assertThat(actual?.giftCertificate).isEqualTo(expected)
    }

    @Test
    fun create() {
        val expected =
            Mono.just(
                CreateGiftCertificateRequest
                    .newBuilder()
                    .setGiftCertificate(giftCertificateConverter.entityToProto(TEST_GIFT_CERTIFICATE))
                    .build()
            )


        val actual = stub.create(expected).block()

        assertThat(actual?.giftCertificate?.name).isEqualTo(TEST_GIFT_CERTIFICATE.name)
    }

    @Test
    fun update() {
        val addedGiftCertificate = giftCertificateService.create(TEST_GIFT_CERTIFICATE_CHANGED).block()!!
        TEST_GIFT_CERTIFICATE.id = addedGiftCertificate.id

        val expected = giftCertificateConverter
            .entityToProto(TEST_GIFT_CERTIFICATE)

        val request =
            Mono.just(
                UpdateGiftCertificateRequest.newBuilder()
                    .setGiftCertificate(expected)
                    .setId(addedGiftCertificate.id)
                    .build()
            )
        val actual =
            stub.update(request).block()

        assertThat(actual?.giftCertificate?.name).isEqualTo(TEST_GIFT_CERTIFICATE.name)
    }

    @Test
    fun deleteById() {
        val giftCertificatesSizeBefore =
            giftCertificateService.getAll(0, 111110).collectList().block()
        val addedGiftCertificate = giftCertificateService.create(TEST_GIFT_CERTIFICATE).block()!!

        val request =
            Mono.just(
                DeleteByIdGiftCertificateRequest.newBuilder()
                    .setGiftCertificateId(addedGiftCertificate.id).build()
            )

        stub.deleteById(request).block()

        val giftCertificatesSizeAfter =
            giftCertificateService.getAll(0, 11110).collectList().block()

        assertThat(giftCertificatesSizeAfter).isEqualTo(giftCertificatesSizeBefore)
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
