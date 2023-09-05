package com.epam.esm.repository

import com.epam.esm.model.entity.GiftCertificate
import org.springframework.data.mongodb.repository.MongoRepository

interface GiftCertificateRepository : MongoRepository<GiftCertificate, String> {
    /**
     * Gets Gift Certificate by  name.
     *
     * @param name Gift Certificate  name to get
     * @return Optional<GiftCertificate> Certificate if founded or Empty if not
     */
    fun findByName(name: String): GiftCertificate?
}
