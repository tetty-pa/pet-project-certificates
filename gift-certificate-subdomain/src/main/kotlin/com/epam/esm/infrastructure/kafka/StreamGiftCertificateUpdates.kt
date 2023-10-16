package com.epam.esm.infrastructure.kafka

import com.epam.esm.GiftCertificateOuterClass.StreamAllGiftCertificatesResponse
import org.springframework.stereotype.Component
import reactor.core.publisher.Flux
import reactor.core.publisher.Sinks

@Component
class StreamGiftCertificateUpdates {

    private final val responseSink: Sinks.Many<StreamAllGiftCertificatesResponse> =
        Sinks.many().multicast().onBackpressureBuffer()

    val flux: Flux<StreamAllGiftCertificatesResponse> = responseSink.asFlux().publish().autoConnect(1)
    fun update(response: StreamAllGiftCertificatesResponse) {
        responseSink.tryEmitNext(response)
    }
}
