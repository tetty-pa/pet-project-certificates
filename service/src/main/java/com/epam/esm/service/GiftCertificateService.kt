package com.epam.esm.service

import com.epam.esm.model.entity.GiftCertificate
import com.mongodb.client.result.DeleteResult
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface GiftCertificateService {
    fun getAll(page: Int, size: Int): Flux<GiftCertificate>

    fun getById(id: String): Mono<GiftCertificate>

    fun create(giftCertificate: GiftCertificate): Mono<GiftCertificate>

    fun update(updatedGiftCertificate: GiftCertificate): Mono<GiftCertificate>

    fun deleteById(id: String): Mono<DeleteResult>
}
