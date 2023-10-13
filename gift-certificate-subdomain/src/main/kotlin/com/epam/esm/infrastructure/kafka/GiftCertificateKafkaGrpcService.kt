package com.epam.esm.infrastructure.kafka

import com.epam.esm.GiftCertificateOuterClass.InitialCertificatesList
import com.epam.esm.GiftCertificateOuterClass.StreamAllGiftCertificatesRequest
import com.epam.esm.GiftCertificateOuterClass.StreamAllGiftCertificatesResponse
import com.epam.esm.application.proto.converter.GiftCertificateConverter
import com.epam.esm.application.service.GiftCertificateService
import com.epam.esm.grpcService.ReactorGiftCertificateKafkaServiceGrpc
import com.epam.esm.web.kafka.StreamGiftCertificateUpdates
import org.springframework.stereotype.Component
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Component
class GiftCertificateKafkaGrpcService(
    private val streamGiftCertificateUpdates: StreamGiftCertificateUpdates,
    private val service: GiftCertificateService,
    private val converter: GiftCertificateConverter
) : ReactorGiftCertificateKafkaServiceGrpc.GiftCertificateKafkaServiceImplBase() {

    override fun streamGiftCertificates(
        streamAllGiftCertificatesRequest: Mono<StreamAllGiftCertificatesRequest>
    ): Flux<StreamAllGiftCertificatesResponse> {
        val initialCertificatesList = getInitialCertificatesListMono()

        return Flux.concat(
            initialCertificatesList.map {
                StreamAllGiftCertificatesResponse.newBuilder().setInitialCertificatesList(it).build()
            },
            streamGiftCertificateUpdates.flux
        )
   }

    private fun getInitialCertificatesListMono(): Mono<InitialCertificatesList> {
        return service.getAll(0, 1000)
            .map { converter.entityToProto(it) }
            .collectList()
            .map {
                InitialCertificatesList.newBuilder().addAllGiftCertificates(it).build()
            }
    }
}
