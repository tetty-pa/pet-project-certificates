package com.epam.esm.web.natsController

import com.epam.esm.GiftCertificateOuterClass.CreateGiftCertificateRequest
import com.epam.esm.GiftCertificateOuterClass.CreateGiftCertificateResponse
import com.epam.esm.GiftCertificateOuterClass.DeleteByIdGiftCertificateRequest
import com.epam.esm.GiftCertificateOuterClass.GetAllGiftCertificateRequest
import com.epam.esm.GiftCertificateOuterClass.GetAllGiftCertificateResponse
import com.epam.esm.GiftCertificateOuterClass.GetByIdGiftCertificateRequest
import com.epam.esm.GiftCertificateOuterClass.GetByIdGiftCertificateResponse
import com.epam.esm.GiftCertificateOuterClass.UpdateGiftCertificateRequest
import com.epam.esm.GiftCertificateOuterClass.UpdateGiftCertificateResponse
import com.epam.esm.NatsSubject
import com.epam.esm.model.entity.GiftCertificate
import com.epam.esm.repository.GiftCertificateRepository
import com.epam.esm.web.converter.GiftCertificateConverter
import io.nats.client.Connection
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import java.math.BigDecimal
import java.time.Duration
import java.time.LocalDateTime

@SpringBootTest
class GiftCertificatesNatsControllerTest {
    @Autowired
    lateinit var natsConnection: Connection

    @Autowired
    lateinit var giftCertificateRepository: GiftCertificateRepository

    @Autowired
    lateinit var giftCertificateConverter: GiftCertificateConverter

    @Test
    fun addGiftCertificateTest() {
        val expected =
            CreateGiftCertificateRequest
                .newBuilder()
                .setGiftCertificate(giftCertificateConverter.entityToProto(TEST_GIFT_CERTIFICATE))
                .build()

        val future = natsConnection.requestWithTimeout(
            NatsSubject.ADD_GIFT_CERTIFICATE_SUBJECT,
            expected.toByteArray(),
            Duration.ofMillis(100000)
        )

        val actual =
            CreateGiftCertificateResponse.parseFrom(future.get().data)

        assertThat(expected.giftCertificate.name).isEqualTo(actual.giftCertificate.name)

        val findByName = giftCertificateRepository.findByName(actual.giftCertificate.name).block()
        if (findByName != null) {
            giftCertificateRepository.deleteById(findByName.id).block()
        }
    }

    @Test
    fun getAllGiftCertificatesTest() {
        val protoList =
            giftCertificateRepository.findAll(PAGE)
                .map { giftCertificate -> giftCertificateConverter.entityToProto(giftCertificate) }.collectList().block()
        val expected = GetAllGiftCertificateResponse.newBuilder()
            .addAllGiftCertificates(protoList)
            .build()

        val request =
            GetAllGiftCertificateRequest.newBuilder()
                .setPage(1).setSize(2).build()

        val future = natsConnection.requestWithTimeout(
            NatsSubject.GET_ALL_GIFT_CERTIFICATES_SUBJECT,
            request.toByteArray(),
            Duration.ofMillis(100000)
        )
        val actual = GetAllGiftCertificateResponse.parseFrom(future.get().data)
        assertThat(expected).isEqualTo(actual)
    }

    @Test
    fun getByIdGiftCertificateTest() {
        val addedGiftCertificate = giftCertificateRepository.save(TEST_GIFT_CERTIFICATE).block()!!
        val dbGiftCertificate = giftCertificateRepository
            .findById(addedGiftCertificate.id).block()!!

        val protoGiftCertificate =
            giftCertificateConverter
                .entityToProto(dbGiftCertificate)

        val expected =
            GetByIdGiftCertificateResponse.newBuilder()
                .setGiftCertificate(protoGiftCertificate).build()

        val request =
            GetByIdGiftCertificateRequest
                .newBuilder()
                .setGiftCertificateId(addedGiftCertificate.id)
                .build()

        val future = natsConnection.requestWithTimeout(
            NatsSubject.GET_GIFT_CERTIFICATE_BY_ID_SUBJECT,
            request.toByteArray(),
            Duration.ofMillis(100000)
        )
        val actual =
            GetByIdGiftCertificateResponse
                .parseFrom(future.get().data)
        giftCertificateRepository.deleteById(addedGiftCertificate.id).block()
        assertThat(expected).isEqualTo(actual)
    }

    @Test
    fun deleteByIdGiftCertificateTest() {
        val giftCertificate = GiftCertificate(
            "1", "1", BigDecimal(1),
            LocalDateTime.now(),
            LocalDateTime.now(), 1, mutableListOf()
        )
        val giftCertificatesSizeBefore = giftCertificateRepository.findAll(Pageable.unpaged()).collectList().block()
        val addedGiftCertificate = giftCertificateRepository.save(giftCertificate).block()!!

        val request =
            DeleteByIdGiftCertificateRequest.newBuilder()
                .setGiftCertificateId(addedGiftCertificate.id).build()
        val future = natsConnection.requestWithTimeout(
            NatsSubject.DELETE_GIFT_CERTIFICATE_BY_ID_SUBJECT,
            request.toByteArray(),
            Duration.ofMillis(1000000)
        )
        future.get().data

        val giftCertificatesSizeAfter = giftCertificateRepository.findAll(Pageable.unpaged()).collectList().block()
        assertThat(giftCertificatesSizeBefore).isEqualTo(giftCertificatesSizeAfter)
    }

    @Test
    fun updateGiftCertificateTest() {
        val addedGiftCertificate = giftCertificateRepository.save(TEST_GIFT_CERTIFICATE).block()!!
        TEST_GIFT_CERTIFICATE_CHANGED.id = addedGiftCertificate.id

        val expected =
            UpdateGiftCertificateResponse.newBuilder()
                .setGiftCertificate(
                    giftCertificateConverter
                        .entityToProto(TEST_GIFT_CERTIFICATE_CHANGED)
                )
                .build()

        val request =
            UpdateGiftCertificateRequest.newBuilder()
                .setGiftCertificate(
                    giftCertificateConverter
                        .entityToProto(TEST_GIFT_CERTIFICATE_CHANGED)
                )
                .setId(addedGiftCertificate.id)
                .build()

        val future = natsConnection.requestWithTimeout(
            NatsSubject.UPDATE_GIFT_CERTIFICATE_SUBJECT,
            request.toByteArray(),
            Duration.ofMillis(100000)
        )
        val actual =
            UpdateGiftCertificateResponse
                .parseFrom(future.get().data)

        assertThat(expected.giftCertificate.name).isEqualTo(actual.giftCertificate.name)

        val findByName = giftCertificateRepository.findByName(actual.giftCertificate.name).block()!!
        findByName.let { giftCertificateRepository.deleteById(it.id) }
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
