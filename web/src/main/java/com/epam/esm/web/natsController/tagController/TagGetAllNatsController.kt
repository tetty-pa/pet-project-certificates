package com.epam.esm.web.natsController.tagController

import com.epam.esm.NatsSubject
import com.epam.esm.TagOuterClass.GetAllTagRequest
import com.epam.esm.TagOuterClass.GetAllTagResponse
import com.epam.esm.service.TagService
import com.epam.esm.web.converter.TagConverter
import com.epam.esm.web.natsController.NatsController
import com.google.protobuf.Parser
import io.nats.client.Connection
import org.springframework.stereotype.Component

@Component
class TagGetAllNatsController(
    private val tagConverter: TagConverter,
    private val service: TagService,
    override val connection: Connection
) : NatsController<GetAllTagRequest, GetAllTagResponse> {

    override val subject: String = NatsSubject.GET_ALL_TAGS_SUBJECT

    override val parser: Parser<GetAllTagRequest> = GetAllTagRequest.parser()

    override fun generateReplyForNatsRequest(request: GetAllTagRequest): GetAllTagResponse {
        val tagListOfProto =
            service.getAll(page = request.page, size = request.size)
                .map { tag -> tagConverter.tagToProto(tag) }
                .toList()

        return GetAllTagResponse.newBuilder()
            .addAllTagList(tagListOfProto)
            .build()
    }
}
