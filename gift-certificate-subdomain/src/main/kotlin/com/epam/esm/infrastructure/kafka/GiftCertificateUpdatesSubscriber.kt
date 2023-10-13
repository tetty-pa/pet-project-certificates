package com.epam.esm.infrastructure.kafka

import com.epam.esm.GiftCertificateOuterClass
import com.epam.esm.grpcService.ReactorGiftCertificateKafkaServiceGrpc
import com.epam.esm.grpcService.ReactorGiftCertificateKafkaServiceGrpc.ReactorGiftCertificateKafkaServiceStub
import io.grpc.ManagedChannel
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Component
class GiftCertificateUpdatesSubscriber(private val grpcChannel: ManagedChannel) {

    private lateinit var stub: ReactorGiftCertificateKafkaServiceStub

    companion object {
        private val LOGGER: Logger = LoggerFactory.getLogger(GiftCertificateUpdatesSubscriber::class.java)
    }

    fun subscribeUpdates() {
        stub = ReactorGiftCertificateKafkaServiceGrpc.newReactorStub(grpcChannel)

        stub.createWithKafka(Flux.from(Mono.just(GiftCertificateOuterClass.CreateGiftCertificateRequest.getDefaultInstance())))
            .doOnNext { LOGGER.info("Update [{}]", it); }
            .subscribe()

        grpcChannel.shutdown()
    }
}
