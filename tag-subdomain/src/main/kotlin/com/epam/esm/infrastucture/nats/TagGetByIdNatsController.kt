package com.epam.esm.infrastucture.nats

import com.epam.esm.NatsSubject
import com.epam.esm.TagOuterClass.GetByIdTagRequest
import com.epam.esm.TagOuterClass.GetByIdTagResponse
import com.epam.esm.application.proto.converter.TagConverter
import com.epam.esm.application.service.TagService
import com.epam.esm.nats.NatsController
import com.google.protobuf.Parser
import io.nats.client.Connection
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class TagGetByIdNatsController(
    private val tagConverter: TagConverter,
    private val service: TagService,
    override val connection: Connection
) : NatsController<GetByIdTagRequest, GetByIdTagResponse> {

    override val subject: String = NatsSubject.GET_TAG_BY_ID_SUBJECT

    override val parser: Parser<GetByIdTagRequest> =
        GetByIdTagRequest.parser()

    override fun generateReplyForNatsRequest(request: GetByIdTagRequest): Mono<GetByIdTagResponse> {

        return service.getById(request.tagId).map {
            GetByIdTagResponse
                .newBuilder()
                .setTag(tagConverter.tagToProto(it))
                .build()
        }
    }
}
