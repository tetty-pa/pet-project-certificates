package com.epam.esm.service

import com.epam.esm.model.entity.GiftCertificate

/**
 * Service interface for Gift Certificate
 */
interface GiftCertificateService {
    /**
     * Gets all Tags.
     *
     * @param page   page number for pagination
     * @param size   page size for pagination
     * @return List of all Gift Certificates
     */
    fun getAll(page: Int, size: Int): List<GiftCertificate>

    /**
     * Gets Gift Certificate by id.
     *
     * @param id Gift Certificate id to get
     * @return GiftCertificate
     */
    fun getById(id: String): GiftCertificate

    /**
     * Creates new Gift Certificate.
     *
     * @param giftCertificate Gift Certificate to create
     * @return created GiftCertificate
     */
    fun create(giftCertificate: GiftCertificate): GiftCertificate

    /**
     * Updates    Gift Certificates.
     *
     * @param updatedGiftCertificate Gift Certificate that needs to be updated
     * @return updated GiftCertificate
     */
    fun update(updatedGiftCertificate: GiftCertificate): GiftCertificate

    /**
     * Deletes Gift Certificates.
     *
     * @param id Gift Certificate id to delete
     */
    fun deleteById(id: String)
}
