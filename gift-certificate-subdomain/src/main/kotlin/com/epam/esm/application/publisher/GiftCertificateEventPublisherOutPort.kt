package com.epam.esm.application.publisher

import com.epam.esm.domain.GiftCertificate
import reactor.core.publisher.Mono
import reactor.kafka.sender.SenderResult

interface GiftCertificateEventPublisherOutPort {
    fun publishGiftCertificateCreatedEvent(giftCertificate: GiftCertificate): Mono<SenderResult<Void>>
}
