package web.grpc.reactor

import assertk.assertThat
import assertk.assertions.isEqualTo
import com.epam.esm.GiftCertificateOuterClass
import com.epam.esm.WebApplication
import com.epam.esm.application.proto.converter.GiftCertificateConverter
import com.epam.esm.application.service.GiftCertificateServiceInPort
import com.epam.esm.domain.GiftCertificate
import com.epam.esm.grpcService.ReactorGiftCertificateServiceGrpc
import com.epam.esm.infrastructure.persistence.repository.mongo.GiftCertificateRepository
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

@SpringBootTest(classes = [WebApplication::class])
class ReactorGiftCertificateGrpcServiceImplTest {
    @Autowired
    lateinit var channel: ManagedChannel

    @Autowired
    private lateinit var giftCertificateConverter: GiftCertificateConverter

    @Autowired
    private lateinit var giftCertificateService: GiftCertificateServiceInPort

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
            GiftCertificateOuterClass.GetAllGiftCertificateResponse.newBuilder().addAllGiftCertificates(
                giftCertificateService.getAll(page = PAGE.pageNumber, size = PAGE.pageSize).map {
                    giftCertificateConverter.entityToProto(it)
                }.collectList().block()
            ).build()

        val request =
            Mono.just(
                GiftCertificateOuterClass.GetAllGiftCertificateRequest.newBuilder()
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
            GiftCertificateOuterClass.GetByIdGiftCertificateResponse.newBuilder()
                .setGiftCertificate(giftCertificateConverter.entityToProto(TEST_GIFT_CERTIFICATE))
                .build()

        val request =
            Mono.just(
                GiftCertificateOuterClass.GetByIdGiftCertificateRequest.newBuilder()
                    .setGiftCertificateId(addedGiftCertificate.id)
                    .build()
            )

        val actual = stub.getById(request)

        StepVerifier.create(actual)
            .assertNext {
                assertThat(expected.giftCertificate.name).isEqualTo(it.giftCertificate.name)
            }
            .verifyComplete()
    }

    @Test
    fun create() {
        val expected =
            GiftCertificateOuterClass.CreateGiftCertificateResponse.newBuilder()
                .setGiftCertificate(giftCertificateConverter.entityToProto(TEST_GIFT_CERTIFICATE))
                .build()

        val request =
            Mono.just(
                GiftCertificateOuterClass.CreateGiftCertificateRequest.newBuilder()
                    .setGiftCertificate(giftCertificateConverter.entityToProto(TEST_GIFT_CERTIFICATE))
                    .build()
            )

        val actual = stub.create(request)

        StepVerifier.create(actual)
            .assertNext {
                assertThat(expected.giftCertificate.name).isEqualTo(it.giftCertificate.name)
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
            GiftCertificateOuterClass.UpdateGiftCertificateResponse.newBuilder()
                .setGiftCertificate(certificate)
                .build()

        val request =
            Mono.just(
                GiftCertificateOuterClass.UpdateGiftCertificateRequest.newBuilder()
                    .setGiftCertificate(certificate)
                    .setId(addedGiftCertificate.id)
                    .build()
            )
        val actual =
            stub.update(request)

        StepVerifier.create(actual)
            .assertNext {
                assertThat(expected.giftCertificate.name).isEqualTo(it.giftCertificate.name)
            }
            .verifyComplete()
    }

    @Test
    fun deleteById() {
        val addedGiftCertificate = giftCertificateService.create(TEST_GIFT_CERTIFICATE).block()!!

        val request =
            Mono.just(
                GiftCertificateOuterClass.DeleteByIdGiftCertificateRequest.newBuilder()
                    .setGiftCertificateId(addedGiftCertificate.id).build()
            )

        val actual = stub.deleteById(request)

        StepVerifier.create(actual)
            .expectNext(GiftCertificateOuterClass.DeleteByIdGiftCertificateResponse.getDefaultInstance())
            .verifyComplete()
    }

    companion object {
        val TEST_GIFT_CERTIFICATE = GiftCertificate(
            null,
            "1", "1", BigDecimal(1),
            LocalDateTime.now(),
            LocalDateTime.now(), 1, mutableListOf()
        )
        val TEST_GIFT_CERTIFICATE_CHANGED = GiftCertificate(
            null,
            "new name", "1", BigDecimal(1),
            LocalDateTime.now(),
            LocalDateTime.now(), 1, mutableListOf()
        )
        val PAGE: PageRequest = PageRequest.of(1, 2)
    }
}
