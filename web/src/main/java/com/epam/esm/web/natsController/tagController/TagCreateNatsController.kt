package com.epam.esm.web.natsController.tagController

import com.epam.esm.NatsSubject
import com.epam.esm.TagOuterClass.CreateTagRequest
import com.epam.esm.TagOuterClass.CreateTagResponse
import com.epam.esm.model.entity.Tag
import com.epam.esm.service.TagService
import com.epam.esm.web.converter.TagConverter
import com.epam.esm.web.natsController.NatsController
import com.google.protobuf.Parser
import io.nats.client.Connection
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class TagCreateNatsController(
    private val tagConverter: TagConverter,
    private val service: TagService,
    override val connection: Connection
) : NatsController<CreateTagRequest, CreateTagResponse> {

    override val subject: String = NatsSubject.ADD_TAG_SUBJECT

    override val parser: Parser<CreateTagRequest> = CreateTagRequest.parser()

    override fun generateReplyForNatsRequest(
        request: CreateTagRequest
    ): Mono<CreateTagResponse> {
        val tag = Tag(request.tag.name)

        return service.create(tag).map {
            CreateTagResponse
                .newBuilder()
                .setTag(tagConverter.tagToProto(it))
                .build()
        }
    }
}
