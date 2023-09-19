package com.epam.esm.web.natsController.tagController

import com.epam.esm.NatsSubject
import com.epam.esm.TagOuterClass.DeleteByIdTagRequest
import com.epam.esm.TagOuterClass.DeleteByIdTagResponse
import com.epam.esm.service.TagService
import com.epam.esm.web.natsController.NatsController
import com.google.protobuf.Parser
import io.nats.client.Connection
import org.springframework.stereotype.Component

@Component
class TagDeleteByIdNatsController(
    private val service: TagService,
    override val connection: Connection
) : NatsController<DeleteByIdTagRequest, DeleteByIdTagResponse> {

    override val subject: String = NatsSubject.DELETE_TAG_BY_ID_SUBJECT

    override val parser: Parser<DeleteByIdTagRequest> = DeleteByIdTagRequest.parser()

    override fun generateReplyForNatsRequest(request: DeleteByIdTagRequest): DeleteByIdTagResponse {
        service.deleteById(request.tagId)
        return DeleteByIdTagResponse
            .newBuilder()
            .build()
    }
}
