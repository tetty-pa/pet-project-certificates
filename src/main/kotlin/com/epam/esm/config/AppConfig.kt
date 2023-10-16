package com.epam.esm.config

import io.grpc.BindableService
import io.grpc.ManagedChannel
import io.grpc.ManagedChannelBuilder
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
class AppConfig(
    @Value("\${grpc.server.port}")
    private val grpcPort: Int,
) {
    @Bean
    fun natsConnection(
        @Value("\${nats.connection.url}")
        natsUrl: String
    ): Connection = Nats.connect(natsUrl)

    @Bean(destroyMethod = "shutdown")
    fun grpcServer(
        grpcServices: List<BindableService>
    ): Server =
        ServerBuilder
            .forPort(grpcPort)
            .apply {
                grpcServices.forEach { addService(it) }
            }
            .build()
            .start()

    @Bean(destroyMethod = "shutdown")
    fun grpcChannel(): ManagedChannel =
        ManagedChannelBuilder
            .forTarget("localhost:$grpcPort")
            .usePlaintext()
            .build()

    @Bean
    fun messageHandlerScheduler(): Scheduler = Schedulers.boundedElastic()
}
