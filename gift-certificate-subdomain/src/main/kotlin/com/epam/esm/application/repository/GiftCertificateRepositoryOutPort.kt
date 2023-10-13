package com.epam.esm.application.repository

import com.epam.esm.domain.GiftCertificate
import com.mongodb.client.result.DeleteResult
import org.springframework.data.domain.Pageable
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface GiftCertificateRepositoryOutPort {
    fun findByName(name: String): Mono<GiftCertificate>

    fun findAll(page: Pageable): Flux<GiftCertificate>

    fun findById(id: String): Mono<GiftCertificate>

    fun save(giftCertificate: GiftCertificate): Mono<GiftCertificate>

    fun deleteById(id: String): Mono<DeleteResult>
}
