package com.epam.esm.infrastructure.nats

import com.epam.esm.GiftCertificateOuterClass.GetAllGiftCertificateRequest
import com.epam.esm.GiftCertificateOuterClass.GetAllGiftCertificateResponse
import com.epam.esm.NatsSubject
import com.epam.esm.application.proto.converter.GiftCertificateConverter
import com.epam.esm.application.service.GiftCertificateServiceInPort
import com.epam.esm.nats.NatsController
import com.google.protobuf.Parser
import io.nats.client.Connection
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class GiftCertificateGetAllNatsController(
    private val giftCertificateConverter: GiftCertificateConverter,
    private val service: GiftCertificateServiceInPort,
    override val connection: Connection
) : NatsController<GetAllGiftCertificateRequest, GetAllGiftCertificateResponse> {

    override val subject: String = NatsSubject.GET_ALL_GIFT_CERTIFICATES_SUBJECT

    override val parser: Parser<GetAllGiftCertificateRequest> = GetAllGiftCertificateRequest.parser()

    override fun generateReplyForNatsRequest(
        request: GetAllGiftCertificateRequest
    ): Mono<GetAllGiftCertificateResponse> {

        return service.getAll(page = request.page, size = request.size)
            .map { giftCertificateConverter.entityToProto(it) }
            .collectList()
            .map {
                GetAllGiftCertificateResponse
                    .newBuilder()
                    .addAllGiftCertificates(it)
                    .build()
            }
    }
}
