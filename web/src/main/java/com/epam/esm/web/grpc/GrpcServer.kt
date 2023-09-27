package com.epam.esm.web.grpc

import com.epam.esm.web.grpc.service.GiftCertificateGrpcServiceImpl
import com.epam.esm.web.grpc.service.TagGrpcServiceImpl
import io.grpc.ServerBuilder
import jakarta.annotation.PostConstruct
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class GrpcServer(
    @Value("\${grpc.server.port}")
    private val grpcPort: Int,
    private val giftCertificateService: GiftCertificateGrpcServiceImpl,
    private val tagService: TagGrpcServiceImpl,
) {
    @PostConstruct
    fun start() {
        val server =
            ServerBuilder
                .forPort(grpcPort)
                .addService(giftCertificateService)
                .addService(tagService)
                .build()
        Thread {
            server.start()
            server.awaitTermination()
        }.start()
    }
}
