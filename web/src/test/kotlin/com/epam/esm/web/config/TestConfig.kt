package com.epam.esm.web.config

import io.grpc.ManagedChannel
import io.grpc.ManagedChannelBuilder
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class TestConfig(
    @Value("\${grpc.server.port}")
    private val grpcPort: Int,
) {
    @Bean(destroyMethod = "shutdown")
    fun grpcChannel(): ManagedChannel =
        ManagedChannelBuilder
            .forTarget("localhost:$grpcPort")
            .usePlaintext()
            .build()
}
