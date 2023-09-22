package com.epam.esm.service.impl

import com.epam.esm.exception.DuplicateEntityException
import com.epam.esm.exception.EntityNotFoundException
import com.epam.esm.model.entity.GiftCertificate
import com.epam.esm.repository.GiftCertificateRepository
import com.epam.esm.service.GiftCertificateService
import com.mongodb.client.result.DeleteResult
import org.springframework.dao.DuplicateKeyException
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.time.LocalDateTime

@Service
class GiftCertificateServiceImpl(
    private val giftCertificateRepository: GiftCertificateRepository
) : GiftCertificateService {

    override fun getAll(page: Int, size: Int): Flux<GiftCertificate> =
        giftCertificateRepository.findAll(PageRequest.of(page, size))

    override fun getById(id: String): Mono<GiftCertificate> {
        return giftCertificateRepository
            .findById(id)
            .switchIfEmpty(
                Mono.error(EntityNotFoundException("gift-certificate.notfoundById"))
            )
    }

    override fun create(giftCertificate: GiftCertificate): Mono<GiftCertificate> {
        giftCertificate.apply {
            createDate = LocalDateTime.now()
            lastUpdatedDate = LocalDateTime.now()
        }
        return giftCertificateRepository.save(giftCertificate)
            .onErrorMap(DuplicateKeyException::class.java) {
                DuplicateEntityException("Duplicate gift certificate error")
            }
    }

    override fun update(updatedGiftCertificate: GiftCertificate): Mono<GiftCertificate> {
        return giftCertificateRepository.findById(updatedGiftCertificate.id)
            .switchIfEmpty(Mono.error(EntityNotFoundException("gift-certificate.notfoundById")))
            .flatMap {
                updatedGiftCertificate.lastUpdatedDate = LocalDateTime.now()
                giftCertificateRepository.save(updatedGiftCertificate)
            }
    }

    override fun deleteById(id: String): Mono<DeleteResult> {
        return giftCertificateRepository.findById(id)
            .switchIfEmpty(Mono.error(EntityNotFoundException("gift-certificate.notfoundById")))
            .flatMap {
                giftCertificateRepository.deleteById(id)
            }
    }
}
