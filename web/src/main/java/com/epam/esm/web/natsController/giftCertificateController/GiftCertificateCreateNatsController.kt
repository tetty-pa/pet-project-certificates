package com.epam.esm.web.natsController.giftCertificateController

import com.epam.esm.GiftCertificateOuterClass.CreateGiftCertificateRequest
import com.epam.esm.GiftCertificateOuterClass.CreateGiftCertificateResponse
import com.epam.esm.NatsSubject
import com.epam.esm.service.GiftCertificateService
import com.epam.esm.web.converter.GiftCertificateConverter
import com.epam.esm.web.natsController.NatsController
import com.google.protobuf.Parser
import io.nats.client.Connection
import org.springframework.stereotype.Component

@Component
class GiftCertificateCreateNatsController(
    private val giftCertificateConverter: GiftCertificateConverter,
    private val service: GiftCertificateService,
    override val connection: Connection
) : NatsController<CreateGiftCertificateRequest, CreateGiftCertificateResponse> {

    override val subject: String = NatsSubject.ADD_GIFT_CERTIFICATE_SUBJECT

    override val parser: Parser<CreateGiftCertificateRequest> =
        CreateGiftCertificateRequest.parser()

    override fun generateReplyForNatsRequest(
        request: CreateGiftCertificateRequest
    ): CreateGiftCertificateResponse {
        val giftCertificate = giftCertificateConverter.protoToEntity(request.giftCertificate)
        val createdGiftCertificate = service.create(giftCertificate)

        return CreateGiftCertificateResponse.newBuilder()
            .setGiftCertificate(
                giftCertificateConverter
                    .entityToProto(createdGiftCertificate)
            )
            .build()
    }
}
