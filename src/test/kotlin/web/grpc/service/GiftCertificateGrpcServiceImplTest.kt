package web.grpc.service

import com.epam.esm.GiftCertificateOuterClass.CreateGiftCertificateRequest
import com.epam.esm.GiftCertificateOuterClass.DeleteByIdGiftCertificateRequest
import com.epam.esm.GiftCertificateOuterClass.GetAllGiftCertificateRequest
import com.epam.esm.GiftCertificateOuterClass.GetByIdGiftCertificateRequest
import com.epam.esm.GiftCertificateOuterClass.UpdateGiftCertificateRequest
import com.epam.esm.WebApplication
import com.epam.esm.application.proto.converter.GiftCertificateConverter
import com.epam.esm.application.service.GiftCertificateServiceInPort
import com.epam.esm.domain.GiftCertificate
import com.epam.esm.grpcService.GiftCertificateServiceGrpc
import com.epam.esm.infrastructure.persistence.repository.mongo.GiftCertificateRepository
import io.grpc.ManagedChannel
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.domain.PageRequest
import java.math.BigDecimal
import java.time.LocalDateTime

@SpringBootTest(classes = [WebApplication::class])
class GiftCertificateGrpcServiceImplTest {
    @Autowired
    private lateinit var channel: ManagedChannel

    @Autowired
    private lateinit var giftCertificateConverter: GiftCertificateConverter

    @Autowired
    private lateinit var giftCertificateService: GiftCertificateServiceInPort

    @Autowired
    private lateinit var giftCertificateRepository: GiftCertificateRepository

    private lateinit var stub: GiftCertificateServiceGrpc.GiftCertificateServiceBlockingStub

    @BeforeEach
    fun startClient() {
        stub = GiftCertificateServiceGrpc.newBlockingStub(channel)
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
            GetAllGiftCertificateRequest.newBuilder()
                .setPage(1).setSize(2).build()

        val actual = stub.getAll(request)

        assertThat(expected).isEqualTo(actual.giftCertificatesList)
    }

    @Test
    fun getById() {
        val addedGiftCertificate =
            giftCertificateService.create(TEST_GIFT_CERTIFICATE).block()!!

        val expected =
            giftCertificateService
                .getById(addedGiftCertificate.id ?: "")
                .map {
                    giftCertificateConverter
                        .entityToProto(it)
                }.block()!!

        val request =
            GetByIdGiftCertificateRequest
                .newBuilder()
                .setGiftCertificateId(addedGiftCertificate.id)
                .build()

        val actual = stub.getById(request)

        assertThat(expected).isEqualTo(actual.giftCertificate)
    }

    @Test
    fun create() {
        val expected =
            CreateGiftCertificateRequest
                .newBuilder()
                .setGiftCertificate(giftCertificateConverter.entityToProto(TEST_GIFT_CERTIFICATE))
                .build()

        val actual = stub.create(expected)

        assertThat(expected.giftCertificate.name).isEqualTo(actual.giftCertificate.name)
    }

    @Test
    fun update() {
        val addedGiftCertificate = giftCertificateService.create(TEST_GIFT_CERTIFICATE_CHANGED).block()!!
        TEST_GIFT_CERTIFICATE.id = addedGiftCertificate.id

        val expected = giftCertificateConverter
            .entityToProto(TEST_GIFT_CERTIFICATE)

        val request =
            UpdateGiftCertificateRequest.newBuilder()
                .setGiftCertificate(expected)
                .setId(addedGiftCertificate.id)
                .build()

        val actual =
            stub.update(request)

        assertThat(TEST_GIFT_CERTIFICATE.name).isEqualTo(actual.giftCertificate.name)
    }

    @Test
    fun deleteById() {
        val giftCertificatesSizeBefore =
            giftCertificateService.getAll(0, 111110).collectList().block()
        val addedGiftCertificate = giftCertificateService.create(TEST_GIFT_CERTIFICATE).block()!!

        val request =
            DeleteByIdGiftCertificateRequest.newBuilder()
                .setGiftCertificateId(addedGiftCertificate.id).build()

        stub.deleteById(request)

        val giftCertificatesSizeAfter =
            giftCertificateService.getAll(0, 11110).collectList().block()

        assertThat(giftCertificatesSizeBefore).isEqualTo(giftCertificatesSizeAfter)
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
