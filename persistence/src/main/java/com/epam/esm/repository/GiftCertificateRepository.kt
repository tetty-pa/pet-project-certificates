package com.epam.esm.repository

import com.epam.esm.model.entity.GiftCertificate
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface GiftCertificateRepository {
    fun findByName(name: String): GiftCertificate?

    fun findAll(page: Pageable): Page<GiftCertificate>

    fun findById(id: String): GiftCertificate?

    fun save(giftCertificate: GiftCertificate): GiftCertificate

    fun deleteById(id: String)
}
