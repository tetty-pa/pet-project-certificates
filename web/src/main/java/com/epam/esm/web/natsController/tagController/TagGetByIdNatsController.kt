package com.epam.esm.web.natsController.tagController

import com.epam.esm.NatsSubject
import com.epam.esm.TagOuterClass.GetByIdTagRequest
import com.epam.esm.TagOuterClass.GetByIdTagResponse
import com.epam.esm.service.TagService
import com.epam.esm.web.converter.TagConverter
import com.epam.esm.web.natsController.NatsController
import com.google.protobuf.Parser
import io.nats.client.Connection
import org.springframework.stereotype.Component

@Component
class TagGetByIdNatsController(
    private val tagConverter: TagConverter,
    private val service: TagService,
    override val connection: Connection
) : NatsController<GetByIdTagRequest, GetByIdTagResponse> {

    override val subject: String = NatsSubject.GET_TAG_BY_ID_SUBJECT

    override val parser: Parser<GetByIdTagRequest> =
        GetByIdTagRequest.parser()

    override fun generateReplyForNatsRequest(request: GetByIdTagRequest): GetByIdTagResponse {
        val tagById = service.getById(request.tagId)
        val protoTag = tagConverter.tagToProto(tagById)

        return GetByIdTagResponse
            .newBuilder()
            .setTag(protoTag)
            .build()
    }
}
