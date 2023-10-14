package com.epam.esm.infrastructure.kafka

import com.epam.esm.GiftCertificateOuterClass.StreamAllGiftCertificatesResponse
import com.epam.esm.KafkaTopic
import com.epam.esm.application.publisher.GiftCertificateEventPublisherOutPort
import com.epam.esm.domain.GiftCertificate
import com.epam.esm.infrastructure.converter.proto.GiftCertificateConverter
import com.google.protobuf.GeneratedMessageV3
import org.springframework.kafka.core.reactive.ReactiveKafkaProducerTemplate
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import reactor.kafka.sender.SenderResult

@Component
class GiftCertificateEventPublisher(
    private val reactiveKafkaProducerTemplate: ReactiveKafkaProducerTemplate<String, GeneratedMessageV3>,
    private val giftCertificateConverter: GiftCertificateConverter
) : GiftCertificateEventPublisherOutPort {
    override fun publishGiftCertificateCreatedEvent(giftCertificate: GiftCertificate): Mono<SenderResult<Void>> {
        return reactiveKafkaProducerTemplate.send(
            KafkaTopic.ADD_GIFT_CERTIFICATE_TOPIC,
            StreamAllGiftCertificatesResponse.newBuilder()
                .setNewGiftCertificate(giftCertificateConverter.domainToProto(giftCertificate))
                .build()
        )
    }
}
