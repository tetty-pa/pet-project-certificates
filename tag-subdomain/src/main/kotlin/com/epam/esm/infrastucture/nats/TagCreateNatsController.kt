package com.epam.esm.infrastucture.nats

import com.epam.esm.NatsSubject
import com.epam.esm.TagOuterClass.CreateTagRequest
import com.epam.esm.TagOuterClass.CreateTagResponse
import com.epam.esm.infrastucture.converter.proto.TagConverter
import com.epam.esm.application.service.TagService
import com.epam.esm.domain.Tag
import com.epam.esm.nats.NatsController
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
        val tag = Tag(null, request.tag.name)

        return service.create(tag).map {
            CreateTagResponse
                .newBuilder()
                .setTag(tagConverter.tagToProto(it))
                .build()
        }
    }
}
