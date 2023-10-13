package com.epam.esm.infrastructure.nats

import com.epam.esm.GiftCertificateOuterClass.UpdateGiftCertificateRequest
import com.epam.esm.GiftCertificateOuterClass.UpdateGiftCertificateResponse
import com.epam.esm.NatsSubject
import com.epam.esm.application.proto.converter.GiftCertificateConverter
import com.epam.esm.application.service.GiftCertificateServiceInPort
import com.epam.esm.nats.NatsController
import com.google.protobuf.Parser
import io.nats.client.Connection
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class GiftCertificateUpdateNatsController(
    private val giftCertificateConverter: GiftCertificateConverter,
    private val service: GiftCertificateServiceInPort,
    override val connection: Connection
) : NatsController<UpdateGiftCertificateRequest, UpdateGiftCertificateResponse> {

    override val subject: String = NatsSubject.UPDATE_GIFT_CERTIFICATE_SUBJECT

    override val parser: Parser<UpdateGiftCertificateRequest> =
        UpdateGiftCertificateRequest.parser()

    override fun generateReplyForNatsRequest(
        request: UpdateGiftCertificateRequest
    ): Mono<UpdateGiftCertificateResponse> {
        val giftCertificate =
            giftCertificateConverter
                .protoToEntity(request.giftCertificate)
                .apply { id = request.id }

        return service.update(giftCertificate)
            .map {
                UpdateGiftCertificateResponse
                    .newBuilder()
                    .setGiftCertificate(giftCertificateConverter.entityToProto(it))
                    .build()
            }
    }
}
