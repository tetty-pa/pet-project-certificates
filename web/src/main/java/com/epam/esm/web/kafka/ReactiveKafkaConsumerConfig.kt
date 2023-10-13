package com.epam.esm.web.kafka

import com.epam.esm.KafkaTopic
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.common.serialization.Deserializer
import org.apache.kafka.common.serialization.StringDeserializer
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.annotation.EnableKafka
import org.springframework.kafka.core.reactive.ReactiveKafkaConsumerTemplate
import reactor.kafka.receiver.ReceiverOptions
import java.util.Collections

@EnableKafka
@Configuration
class ReactiveKafkaConsumerConfig {

    @Value("\${spring.kafka.bootstrap-servers}")
    private lateinit var bootstrapAddress: String

    @Bean
    fun receiverOptions(): ReceiverOptions<String, ByteArray> {
        return ReceiverOptions.create<String, ByteArray>(
            mapOf(
                Pair(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress),
                Pair(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer::class.java),
                Pair(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ProtobufDeserializer::class.java),
                Pair(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest"),
                Pair(ConsumerConfig.GROUP_ID_CONFIG, "ajax"),
            )
        ).subscription(Collections.singletonList(KafkaTopic.ADD_GIFT_CERTIFICATE_TOPIC))
    }

    @Bean
    fun reactiveKafkaConsumerTemplate(
        receiverOptions: ReceiverOptions<String, ByteArray>
    ): ReactiveKafkaConsumerTemplate<String, ByteArray> {
        return ReactiveKafkaConsumerTemplate(receiverOptions)
    }
}

class ProtobufDeserializer : Deserializer<ByteArray> {
    override fun deserialize(topic: String, data: ByteArray): ByteArray {
        return data
    }
}
