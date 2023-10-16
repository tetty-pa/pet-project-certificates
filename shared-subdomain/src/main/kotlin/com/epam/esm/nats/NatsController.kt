package com.epam.esm.nats

import com.google.protobuf.GeneratedMessageV3
import com.google.protobuf.Parser
import io.nats.client.Connection
import io.nats.client.Message
import reactor.core.publisher.Mono

interface NatsController<ReqT : GeneratedMessageV3, RespT : GeneratedMessageV3> {

    val subject: String

    val connection: Connection

    val parser: Parser<ReqT>

    fun generateReplyForNatsRequest(request: ReqT): Mono<RespT>

    fun handle(msg: Message): Mono<RespT> {
        val request: ReqT = parser.parseFrom(msg.data)
        return generateReplyForNatsRequest(request)
    }
}
