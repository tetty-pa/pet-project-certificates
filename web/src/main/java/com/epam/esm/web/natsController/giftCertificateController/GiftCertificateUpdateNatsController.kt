package com.epam.esm.web.natsController.giftCertificateController

import com.epam.esm.GiftCertificateOuterClass.UpdateGiftCertificateRequest
import com.epam.esm.GiftCertificateOuterClass.UpdateGiftCertificateResponse
import com.epam.esm.NatsSubject
import com.epam.esm.service.GiftCertificateService
import com.epam.esm.web.converter.GiftCertificateConverter
import com.epam.esm.web.natsController.NatsController
import com.google.protobuf.Parser
import io.nats.client.Connection
import org.springframework.stereotype.Component

@Component
class GiftCertificateUpdateNatsController(
    private val giftCertificateConverter: GiftCertificateConverter,
    private val service: GiftCertificateService,
    override val connection: Connection
) : NatsController<UpdateGiftCertificateRequest, UpdateGiftCertificateResponse> {

    override val subject: String = NatsSubject.UPDATE_GIFT_CERTIFICATE_SUBJECT

    override val parser: Parser<UpdateGiftCertificateRequest> =
        UpdateGiftCertificateRequest.parser()

    override fun generateReplyForNatsRequest(request: UpdateGiftCertificateRequest): UpdateGiftCertificateResponse {
        val giftCertificate =
            giftCertificateConverter
                .protoToEntity(request.giftCertificate)
                .apply { id = request.id }
        val createdGiftCertificate = service.update(giftCertificate)

        return UpdateGiftCertificateResponse.newBuilder()
            .setGiftCertificate(
                giftCertificateConverter
                    .entityToProto(createdGiftCertificate)
            )
            .build()
    }
}
