package com.epam.esm.web.natsController.giftCertificateController

import com.epam.esm.GiftCertificateList.GetAllGiftCertificateRequest
import com.epam.esm.GiftCertificateList.GetAllGiftCertificateResponse
import com.epam.esm.GiftCertificateList.ListOfGiftCertificates
import com.epam.esm.NatsSubject
import com.epam.esm.service.GiftCertificateService
import com.epam.esm.web.converter.GiftCertificateConverter
import com.epam.esm.web.natsController.NatsController
import com.google.protobuf.Parser
import io.nats.client.Connection
import org.springframework.stereotype.Component

@Component
class GiftCertificateGetAllNatsController(
    private val giftCertificateConverter: GiftCertificateConverter,
    private val service: GiftCertificateService,
    override val connection: Connection
) : NatsController<GetAllGiftCertificateRequest, GetAllGiftCertificateResponse> {

    override val subject: String = NatsSubject.GET_ALL_GIFT_CERTIFICATES_SUBJECT

    override val parser: Parser<GetAllGiftCertificateRequest> = GetAllGiftCertificateRequest.parser()

    override fun generateReplyForNatsRequest(
        request: GetAllGiftCertificateRequest
    ): GetAllGiftCertificateResponse {
        val giftCertificateListOfProto =
            service.getAll(page = request.page, size = request.size)
                .map { tag -> giftCertificateConverter.entityToProto(tag) }
                .toList()

        val listOfGiftCertificates =
            ListOfGiftCertificates.newBuilder().addAllGiftCertificates(giftCertificateListOfProto)
        return GetAllGiftCertificateResponse.newBuilder()
            .setGiftCertificateList(listOfGiftCertificates)
            .build()
    }
}
