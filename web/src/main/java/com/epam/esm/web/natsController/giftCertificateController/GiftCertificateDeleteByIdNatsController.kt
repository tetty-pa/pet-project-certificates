package com.epam.esm.web.natsController.giftCertificateController

import com.epam.esm.GiftCertificateOuterClass.DeleteByIdGiftCertificateRequest
import com.epam.esm.GiftCertificateOuterClass.DeleteByIdGiftCertificateResponse
import com.epam.esm.NatsSubject
import com.epam.esm.service.GiftCertificateService
import com.epam.esm.web.natsController.NatsController
import com.google.protobuf.Parser
import io.nats.client.Connection
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class GiftCertificateDeleteByIdNatsController(
    private val service: GiftCertificateService,
    override val connection: Connection
) : NatsController<DeleteByIdGiftCertificateRequest, DeleteByIdGiftCertificateResponse> {

    override val subject: String = NatsSubject.DELETE_GIFT_CERTIFICATE_BY_ID_SUBJECT

    override val parser: Parser<DeleteByIdGiftCertificateRequest> =
        DeleteByIdGiftCertificateRequest.parser()

    override fun generateReplyForNatsRequest(
        request: DeleteByIdGiftCertificateRequest
    ): Mono<DeleteByIdGiftCertificateResponse> {

        return service.deleteById(request.giftCertificateId)
            .map {
                DeleteByIdGiftCertificateResponse.getDefaultInstance()
            }
    }
}
