package com.epam.esm.application.service

import com.epam.esm.domain.GiftCertificate
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

/**
 * Service interface for Gift Certificate
 */
interface GiftCertificateServiceInPort {
    /**
     * Gets all Tags.
     *
     * @param page   page number for pagination
     * @param size   page size for pagination
     * @return List of all Gift Certificates Flux<GiftCertificate>
     */
    fun getAll(page: Int, size: Int): Flux<GiftCertificate>

    /**
     * Gets Gift Certificate by id.
     *
     * @param id Gift Certificate id to get
     * @return Mono<GiftCertificate>
     */
    fun getById(id: String): Mono<GiftCertificate>

    /**
     * Creates new Gift Certificate.
     *
     * @param giftCertificate Gift Certificate to create
     * @return created Mono<GiftCertificate>
     */
    fun create(giftCertificate: GiftCertificate): Mono<GiftCertificate>

    /**
     * Updates Gift Certificates.
     *
     * @param updatedGiftCertificate Gift Certificate that needs to be updated
     * @return updated Mono<GiftCertificate>
     */
    fun update(updatedGiftCertificate: GiftCertificate): Mono<GiftCertificate>

    /**
     * Deletes Gift Certificates.
     *
     * @param id Gift Certificate id to delete
     * @return Mono<Unit>
     */
    fun deleteById(id: String): Mono<Unit>
}
