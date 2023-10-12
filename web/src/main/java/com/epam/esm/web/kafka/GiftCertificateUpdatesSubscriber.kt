package com.epam.esm.web.kafka

import com.epam.esm.GiftCertificateOuterClass.StreamAllGiftCertificatesResponse
import com.epam.esm.NatsSubject
import io.nats.client.Connection
import jakarta.annotation.PostConstruct
import org.springframework.stereotype.Component

@Component
class GiftCertificateUpdatesSubscriber(
    private val natsConnection: Connection,
    private val streamGiftCertificateUpdates: StreamGiftCertificateUpdates,
) {

    @PostConstruct
    fun subscribeUpdates() {
        natsConnection.createDispatcher { message ->
            val parsedData =
                StreamAllGiftCertificatesResponse.parseFrom(message.data)
            streamGiftCertificateUpdates.update(parsedData)
        }.subscribe(NatsSubject.NATS_ADD_GIFT_CERTIFICATE_SUBJECT)
    }
}
