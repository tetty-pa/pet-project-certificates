package com.epam.esm.infrastructure.nats

import com.epam.esm.GiftCertificateOuterClass.GetByIdGiftCertificateRequest
import com.epam.esm.GiftCertificateOuterClass.GetByIdGiftCertificateResponse
import com.epam.esm.NatsSubject
import com.epam.esm.infrastructure.converter.proto.GiftCertificateConverter
import com.epam.esm.application.service.GiftCertificateServiceInPort
import com.epam.esm.nats.NatsController
import com.google.protobuf.Parser
import io.nats.client.Connection
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class GiftCertificateGetByIdNatsController(
    private val giftCertificateConverter: GiftCertificateConverter,
    private val service: GiftCertificateServiceInPort,
    override val connection: Connection
) : NatsController<GetByIdGiftCertificateRequest, GetByIdGiftCertificateResponse> {

    override val subject: String = NatsSubject.GET_GIFT_CERTIFICATE_BY_ID_SUBJECT

    override val parser: Parser<GetByIdGiftCertificateRequest> =
        GetByIdGiftCertificateRequest.parser()

    override fun generateReplyForNatsRequest(
        request: GetByIdGiftCertificateRequest
    ): Mono<GetByIdGiftCertificateResponse> {

        return service.getById(request.giftCertificateId)
            .map {
                GetByIdGiftCertificateResponse
                    .newBuilder()
                    .setGiftCertificate(giftCertificateConverter.domainToProto((it)))
                    .build()
            }
    }
}
