package com.epam.esm.config

import io.nats.client.Connection
import io.nats.client.Nats
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@EntityScan(basePackages = ["com.epam.esm"])
class AppConfig {
    @Bean
    fun natsConnection(
        @Value("\${nats.connection.url}")
        natsUrl: String
    ): Connection = Nats.connect(natsUrl)
}
