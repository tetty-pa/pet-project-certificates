package com.epam.esm.infrastructure.kafka

import com.epam.esm.NatsSubject
import io.nats.client.Connection
import org.springframework.boot.CommandLineRunner
import org.springframework.context.annotation.Bean
import org.springframework.kafka.core.reactive.ReactiveKafkaConsumerTemplate
import org.springframework.stereotype.Component

@Component
class KafkaAddGiftCertificateEventListener(
    private val natsConnection: Connection,
    private val reactiveKafkaConsumerTemplate: ReactiveKafkaConsumerTemplate<String, ByteArray>
) {
    @Bean
    fun run(): CommandLineRunner {
        return CommandLineRunner {
            reactiveKafkaConsumerTemplate
                .receiveAutoAck()
                .map { it.value() }
                .doOnNext { natsConnection.publish(NatsSubject.NATS_ADD_GIFT_CERTIFICATE_SUBJECT, it) }
                .subscribe()
        }
    }
}
