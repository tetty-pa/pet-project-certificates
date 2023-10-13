package com.epam.esm.infrastucture.nats

import com.epam.esm.NatsSubject
import com.epam.esm.TagOuterClass.DeleteByIdTagRequest
import com.epam.esm.TagOuterClass.DeleteByIdTagResponse
import com.epam.esm.application.service.TagService
import com.epam.esm.nats.NatsController
import com.google.protobuf.Parser
import io.nats.client.Connection
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class TagDeleteByIdNatsController(
    private val service: TagService,
    override val connection: Connection
) : NatsController<DeleteByIdTagRequest, DeleteByIdTagResponse> {

    override val subject: String = NatsSubject.DELETE_TAG_BY_ID_SUBJECT

    override val parser: Parser<DeleteByIdTagRequest> = DeleteByIdTagRequest.parser()

    override fun generateReplyForNatsRequest(
        request: DeleteByIdTagRequest
    ): Mono<DeleteByIdTagResponse> {

        return service.deleteById(request.tagId).map {
            DeleteByIdTagResponse.getDefaultInstance()
        }
    }
}
