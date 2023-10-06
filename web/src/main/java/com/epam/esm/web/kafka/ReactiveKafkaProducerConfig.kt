package com.epam.esm.web.kafka

import com.google.protobuf.GeneratedMessageV3
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.common.serialization.Serializer
import org.apache.kafka.common.serialization.StringSerializer
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.core.reactive.ReactiveKafkaProducerTemplate
import reactor.kafka.sender.SenderOptions


@Configuration
class ReactiveKafkaProducerConfig {

    @Value("\${spring.kafka.bootstrap-servers}")
    private lateinit var bootstrapAddress: String

    @Bean
    fun senderOptions(): SenderOptions<String, GeneratedMessageV3> {
        return SenderOptions.create(
            mapOf(
                Pair(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress),
                Pair(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer::class.java),
                Pair(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, ProtobufSerializer::class.java)
            )
        )
    }

    @Bean
    fun reactiveKafkaProducerTemplate(
        senderOptions: SenderOptions<String, GeneratedMessageV3>
    ): ReactiveKafkaProducerTemplate<String, GeneratedMessageV3> {
        return ReactiveKafkaProducerTemplate(senderOptions)
    }
}

class ProtobufSerializer<T : GeneratedMessageV3> : Serializer<T> {
    override fun serialize(topic: String, data: T): ByteArray {
        return data.toByteArray()
    }
}
