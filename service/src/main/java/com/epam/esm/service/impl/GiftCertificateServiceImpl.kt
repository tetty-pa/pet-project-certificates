package com.epam.esm.service.impl

import com.epam.esm.exception.DuplicateEntityException
import com.epam.esm.exception.EntityNotFoundException
import com.epam.esm.model.entity.GiftCertificate
import com.epam.esm.model.entity.Tag
import com.epam.esm.model.entity.util.QueryParameters
import com.epam.esm.model.entity.util.SortType
import com.epam.esm.repository.GiftCertificateRepository
import com.epam.esm.repository.TagRepository
import com.epam.esm.service.GiftCertificateService
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.time.ZoneId
import javax.transaction.Transactional

@Service
class GiftCertificateServiceImpl(
    private val giftCertificateRepository: GiftCertificateRepository,
    private val tagRepository: TagRepository
) : GiftCertificateService {
    override fun getAll(page: Int, size: Int): List<GiftCertificate> {
        val pageRequest = PageRequest.of(page, size)

        return giftCertificateRepository.findAll(pageRequest).content

    }

    override fun getGiftCertificatesByParameters(
        queryParameters: QueryParameters,
        page: Int,
        size: Int
    ): List<GiftCertificate> {
        val tagList = getTags(queryParameters)
        val sort = getSort(queryParameters)

        val pageRequest = sort?.let { PageRequest.of(page, size, it) }
            ?: PageRequest.of(page, size)

        val partOfName = getPartParameter(queryParameters.partOfName)

        val partOfDescription = getPartParameter(queryParameters.partOfDescription)

        return giftCertificateRepository
            .getGiftCertificatesByNameLikeAndDescriptionLikeAndTagListIn(
                partOfName,
                partOfDescription,
                tagList,
                pageRequest
            )
    }

    private fun getTags(queryParameters: QueryParameters): List<Tag> {
        val tagList = mutableListOf<Tag>()
        queryParameters.tagNames?.let { tagNames ->
            tagNames.forEach { tagName ->
                val tag = tagName?.let {
                    tagRepository.findByName(it)
                        .orElseThrow { EntityNotFoundException("tag.notfoundByName") }
                }
                if (tag != null) {
                    tagList.add(tag)
                }
            }
        } ?: tagList.addAll(tagRepository.findAll())
        return tagList
    }


    private fun getSort(queryParameters: QueryParameters): Sort? {
        var sort: Sort? = null

        queryParameters.sortDateParam?.let { sortDateParam ->
            sort = Sort.by(Sort.Order.asc("createDate"))
            if (sortDateParam == SortType.DESC)
                sort = Sort.by(Sort.Order.desc("createDate"))
        }

        queryParameters.sortNameParam?.let { sortNameParam ->
            sort?.let {
                sort = it.and(Sort.by(Sort.Order.asc("name")))
            } ?: Sort.by(Sort.Order.desc("name"))

            if (sortNameParam == SortType.DESC)
                sort = Sort.by(Sort.Order.desc("name"))
        }
        return sort
    }

    private fun getPartParameter(partOfParameter: String): String =  "%$partOfParameter%"


    override fun getById(id: Long): GiftCertificate {
        return giftCertificateRepository.findById(id)
            .orElseThrow { EntityNotFoundException("gift-certificate.notfoundById") }
    }

    @Transactional
    override fun create(giftCertificate: GiftCertificate): GiftCertificate {
        val isCertificateExist = giftCertificateRepository.findByName(giftCertificate.name).isPresent
        if (isCertificateExist) throw DuplicateEntityException("gift-certificate.already.exist")

        val tagsToPersist = giftCertificate.tagList.filter { tag ->
            val tagOptional = tagRepository.findByName(tag.name)
            tagOptional.isEmpty || tag.id != tagOptional.get().id
        } ?: emptyList()

        giftCertificate.apply {
            createDate = LocalDateTime.now().atZone(ZoneId.of("Europe/Kiev"))
            lastUpdatedDate = LocalDateTime.now().atZone(ZoneId.of("Europe/Kiev"))
            tagList = tagsToPersist.toMutableList()
        }

        return giftCertificateRepository.save(giftCertificate)

    }


    @Transactional
    override fun update(giftCertificate: GiftCertificate): GiftCertificate {
        val id = giftCertificate.id
        val oldGiftCertificate = giftCertificateRepository.findById(id)
            .orElseThrow { EntityNotFoundException("gift-certificate.notfoundById") }

        findUpdatedFields(oldGiftCertificate, giftCertificate)
        val tagList = oldGiftCertificate.tagList
        oldGiftCertificate.tagList = updateTags(tagList).toMutableList()
        giftCertificate.lastUpdatedDate = LocalDateTime.now().atZone(ZoneId.of("Europe/Kiev"))
        return giftCertificateRepository.save(giftCertificate)
    }

    private fun updateTags(tags: List<Tag>): List<Tag> {
        return tags.map { tag ->
            val tagOptional = tagRepository.findByName(tag.name)
            if (tagOptional.isEmpty || tag.id != tagOptional.get().id) {
                throw EntityNotFoundException("tag.incorrectName")
            }
            tagOptional.get()
        }
    }

    private fun findUpdatedFields(giftCertificate: GiftCertificate, updatedGiftCertificate: GiftCertificate) {

        updatePropertyIfNeeded(giftCertificate.name, updatedGiftCertificate.name) { giftCertificate.name = it }
        updatePropertyIfNeeded(
            giftCertificate.description,
            updatedGiftCertificate.description
        ) { giftCertificate.description = it }
        updatePropertyIfNeeded(giftCertificate.price, updatedGiftCertificate.price) { giftCertificate.price = it }
        updatePropertyIfNeeded(giftCertificate.duration, updatedGiftCertificate.duration) {
            giftCertificate.duration = it
        }

    }

    private fun <T> updatePropertyIfNeeded(
        currentValue: T?,
        updatedValue: T?,
        updateAction: (T) -> Unit
    ) {
        if (updatedValue != null && updatedValue != currentValue) {
            updateAction(updatedValue)
        }
    }

    override fun deleteById(id: Long) {
        giftCertificateRepository.findById(id)
            .orElseThrow { EntityNotFoundException("gift-certificate.notfoundById") }
        giftCertificateRepository.deleteById(id)
    }
}