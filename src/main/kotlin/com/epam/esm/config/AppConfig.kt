package com.epam.esm.config

import com.epam.esm.infrastructure.persistence.entity.GiftCertificateEntity
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
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory
import org.springframework.data.redis.core.ReactiveRedisTemplate
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer
import org.springframework.data.redis.serializer.RedisSerializationContext
import org.springframework.data.redis.serializer.StringRedisSerializer
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


    @Bean
    fun reactiveRedisTemplate(connectionFactory: ReactiveRedisConnectionFactory): ReactiveRedisTemplate<String, GiftCertificateEntity> {
        val valueSerializer =
            Jackson2JsonRedisSerializer(GiftCertificateEntity::class.java)

        val serializationContext =
            RedisSerializationContext.newSerializationContext<String, GiftCertificateEntity>(StringRedisSerializer())
                .value(valueSerializer)
                .hashValue(valueSerializer)
                .build()

        return ReactiveRedisTemplate(connectionFactory, serializationContext)

    }

}
