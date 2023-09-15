package com.epam.esm.web.natsController

import com.google.protobuf.GeneratedMessageV3
import com.google.protobuf.Parser
import io.nats.client.Connection
import io.nats.client.Message

interface NatsController<ReqT : GeneratedMessageV3, RespT : GeneratedMessageV3> {
    val subject: String
    val connection: Connection
    val parser: Parser<ReqT>
    fun generateReplyForNatsRequest(request: ReqT): RespT
    fun handle(msg: Message): RespT {
        val request: ReqT = parser.parseFrom(msg.data)
        return generateReplyForNatsRequest(request)
    }
}
