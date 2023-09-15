package com.epam.esm.web.natsController.giftCertificateController

import com.epam.esm.GiftCertificateOuterClass.GetByIdGiftCertificateRequest
import com.epam.esm.GiftCertificateOuterClass.GetByIdGiftCertificateResponse
import com.epam.esm.NatsSubject
import com.epam.esm.service.GiftCertificateService
import com.epam.esm.web.converter.GiftCertificateConverter
import com.epam.esm.web.natsController.NatsController
import com.google.protobuf.Parser
import io.nats.client.Connection
import org.springframework.stereotype.Component

@Component
class GiftCertificateGetByIdNatsController(
    private val giftCertificateConverter: GiftCertificateConverter,
    private val service: GiftCertificateService,
    override val connection: Connection
) : NatsController<GetByIdGiftCertificateRequest, GetByIdGiftCertificateResponse> {

    override val subject: String = NatsSubject.GET_GIFT_CERTIFICATE_BY_ID_SUBJECT

    override val parser: Parser<GetByIdGiftCertificateRequest> =
        GetByIdGiftCertificateRequest.parser()

    override fun generateReplyForNatsRequest(
        request: GetByIdGiftCertificateRequest
    ): GetByIdGiftCertificateResponse {

        val giftCertificateById = service.getById(request.giftCertificateId)
        val protoTag =
            giftCertificateConverter
                .entityToProto(giftCertificateById)

        return GetByIdGiftCertificateResponse
            .newBuilder()
            .setGiftCertificate(protoTag)
            .build()
    }
}
