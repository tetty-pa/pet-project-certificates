package com.epam.esm.service.impl

import com.epam.esm.exception.DuplicateEntityException
import com.epam.esm.exception.EntityNotFoundException
import com.epam.esm.model.entity.GiftCertificate
import com.epam.esm.repository.GiftCertificateRepository
import com.epam.esm.service.GiftCertificateService
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal
import java.time.LocalDateTime

@Service
class GiftCertificateServiceImpl(
    private val giftCertificateRepository: GiftCertificateRepository
) : GiftCertificateService {
    override fun getAll(page: Int, size: Int): List<GiftCertificate> =
        giftCertificateRepository.findAll(PageRequest.of(page, size)).content

    override fun getById(id: String): GiftCertificate =
        giftCertificateRepository.findById(id)
            .orElseThrow { EntityNotFoundException("gift-certificate.notfoundById") }

    @Transactional
    override fun create(giftCertificate: GiftCertificate): GiftCertificate {
        giftCertificateRepository.findByName(giftCertificate.name)
            ?: throw DuplicateEntityException("gift-certificate.already.exist")

        giftCertificate.apply {
            createDate = LocalDateTime.now()
            lastUpdatedDate = LocalDateTime.now()
        }

        return giftCertificateRepository.save(giftCertificate)
    }

    @Transactional
    override fun update(updatedGiftCertificate: GiftCertificate): GiftCertificate {
        val id = updatedGiftCertificate.id
        val giftCertificate = giftCertificateRepository.findById(id)
            .orElseThrow { EntityNotFoundException("gift-certificate.notfoundById") }

        updateFields(giftCertificate, updatedGiftCertificate)
        giftCertificate.lastUpdatedDate = LocalDateTime.now()
        return giftCertificateRepository.save(giftCertificate)
    }

    private fun updateFields(giftCertificate: GiftCertificate, updatedGiftCertificate: GiftCertificate) {
        with(updatedGiftCertificate) {
            if (name != "") {
                giftCertificate.name = name
            }
            if (description != "") {
                giftCertificate.description = description
            }
            if (price != BigDecimal.ZERO) {
                giftCertificate.price = price
            }
            if (duration != 0) {
                giftCertificate.duration = duration
            }
        }
    }

    override fun deleteById(id: String) {
        giftCertificateRepository.findById(id)
            .orElseThrow { EntityNotFoundException("gift-certificate.notfoundById") }
        giftCertificateRepository.deleteById(id)
    }
}
