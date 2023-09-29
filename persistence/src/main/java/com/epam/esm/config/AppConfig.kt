package com.epam.esm.config

import io.grpc.BindableService
import io.grpc.Server
import io.grpc.ServerBuilder
import io.nats.client.Connection
import io.nats.client.Nats
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import reactor.core.scheduler.Scheduler
import reactor.core.scheduler.Schedulers

@Configuration
@EntityScan(basePackages = ["com.epam.esm"])
class AppConfig {
    @Bean
    fun natsConnection(
        @Value("\${nats.connection.url}")
        natsUrl: String
    ): Connection = Nats.connect(natsUrl)

    @Bean
    fun grpcServer(
        @Value("\${grpc.server.port}")
        grpcPort: Int,
        grpcServices: List<BindableService>
    ): Server =
        ServerBuilder
            .forPort(grpcPort)
            .apply {
                grpcServices.forEach { addService(it) }
            }
            .build()
            .start()

    @Bean
    fun messageHandlerScheduler(): Scheduler = Schedulers.boundedElastic()
}
