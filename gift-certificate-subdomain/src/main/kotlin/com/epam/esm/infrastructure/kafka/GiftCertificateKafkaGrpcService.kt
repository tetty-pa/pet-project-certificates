package com.epam.esm.infrastructure.kafka

import com.epam.esm.GiftCertificateOuterClass.CreateGiftCertificateRequest
import com.epam.esm.GiftCertificateOuterClass.CreateGiftCertificateResponse
import com.epam.esm.NatsSubject
import com.epam.esm.grpcService.ReactorGiftCertificateKafkaServiceGrpc
import io.nats.client.Connection
import jakarta.annotation.PostConstruct
import org.springframework.stereotype.Component
import reactor.core.publisher.Flux
import reactor.core.publisher.Sinks

@Component
class GiftCertificateKafkaGrpcService(
    private val natsConnection: Connection
) : ReactorGiftCertificateKafkaServiceGrpc.GiftCertificateKafkaServiceImplBase() {

    private val responseSink: Sinks.Many<CreateGiftCertificateResponse> =
        Sinks.many().multicast().onBackpressureBuffer()

    @PostConstruct
    fun listenToEvents() {
        val dispatcher = natsConnection.createDispatcher { message ->
            responseSink.tryEmitNext(CreateGiftCertificateResponse.parseFrom(message.data))
        }
        dispatcher.subscribe(NatsSubject.NATS_ADD_GIFT_CERTIFICATE_SUBJECT)
    }

    override fun createWithKafka(request: Flux<CreateGiftCertificateRequest>): Flux<CreateGiftCertificateResponse> {
        return responseSink.asFlux()
    }
}
