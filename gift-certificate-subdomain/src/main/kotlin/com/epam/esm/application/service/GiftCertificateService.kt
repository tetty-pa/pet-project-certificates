package com.epam.esm.application.service

import com.epam.esm.GiftCertificateOuterClass.StreamAllGiftCertificatesResponse
import com.epam.esm.KafkaTopic
import com.epam.esm.application.proto.converter.GiftCertificateConverter
import com.epam.esm.application.repository.GiftCertificateRepositoryOutPort
import com.epam.esm.domain.GiftCertificate
import com.epam.esm.exception.DuplicateEntityException
import com.epam.esm.exception.EntityNotFoundException
import com.google.protobuf.GeneratedMessageV3
import com.mongodb.client.result.DeleteResult
import org.springframework.dao.DuplicateKeyException
import org.springframework.data.domain.PageRequest
import org.springframework.kafka.core.reactive.ReactiveKafkaProducerTemplate
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.time.LocalDateTime

@Service
class GiftCertificateService(
    private val giftCertificateRepository: GiftCertificateRepositoryOutPort,
    private val reactiveKafkaProducerTemplate: ReactiveKafkaProducerTemplate<String, GeneratedMessageV3>,
    private val giftCertificateConverter: GiftCertificateConverter
) : GiftCertificateServiceInPort {

    override fun getAll(page: Int, size: Int): Flux<GiftCertificate> =
        giftCertificateRepository.findAll(PageRequest.of(page, size))

    override fun getById(id: String): Mono<GiftCertificate> {
        return giftCertificateRepository.findById(id)
            .switchIfEmpty(
                Mono.error(EntityNotFoundException("gift-certificate.notfoundById"))
            )
    }

    override fun create(giftCertificate: GiftCertificate): Mono<GiftCertificate> {
        giftCertificate.apply {
            createDate = LocalDateTime.now()
            lastUpdatedDate = LocalDateTime.now()
        }
        return giftCertificateRepository.save(giftCertificate).onErrorMap(DuplicateKeyException::class.java) {
            DuplicateEntityException("Duplicate gift certificate error")
        }  .flatMap {
            reactiveKafkaProducerTemplate.send(
                KafkaTopic.ADD_GIFT_CERTIFICATE_TOPIC,
                StreamAllGiftCertificatesResponse.newBuilder()
                    .setNewGiftCertificate(giftCertificateConverter.entityToProto(it))
                    .build()
            ).thenReturn(it)
        }
    }

    override fun update(updatedGiftCertificate: GiftCertificate): Mono<GiftCertificate> {
        return giftCertificateRepository.findById(updatedGiftCertificate.id ?: "")
            .switchIfEmpty(Mono.error(EntityNotFoundException("gift-certificate.notfoundById"))).flatMap {
                updatedGiftCertificate.lastUpdatedDate = LocalDateTime.now()
                giftCertificateRepository.save(updatedGiftCertificate)
            }
    }

    override fun deleteById(id: String): Mono<DeleteResult> {
        return giftCertificateRepository.findById(id)
            .switchIfEmpty(Mono.error(EntityNotFoundException("gift-certificate.notfoundById"))).flatMap {
                giftCertificateRepository.deleteById(id)
            }
    }
}
