package com.epam.esm.infrastructure.mongo.mapper

import com.epam.esm.domain.GiftCertificate
import com.epam.esm.infrastructure.mongo.entity.GiftCertificateEntity
import com.epam.esm.mapper.EntityMapper
import org.springframework.stereotype.Component

@Component
class GiftCertificateMapper : EntityMapper<GiftCertificate, GiftCertificateEntity> {
    override fun mapToDomain(entity: GiftCertificateEntity): GiftCertificate {
        return GiftCertificate(
            id = entity.id,
            name = entity.name,
            description = entity.description,
            price = entity.price,
            createDate = entity.createDate,
            lastUpdatedDate = entity.lastUpdatedDate,
            duration = entity.duration,
            tagList = entity.tagList
        )
    }

    override fun mapToEntity(domain: GiftCertificate): GiftCertificateEntity {
        return GiftCertificateEntity(
            name = domain.name,
            description = domain.description,
            price = domain.price,
            createDate = domain.createDate,
            lastUpdatedDate = domain.lastUpdatedDate,
            duration = domain.duration,
            tagList = domain.tagList
        )
    }
}
