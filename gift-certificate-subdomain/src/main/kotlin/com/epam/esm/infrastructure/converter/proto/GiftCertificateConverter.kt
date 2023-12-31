package com.epam.esm.infrastructure.converter.proto

import com.epam.esm.GiftCertificateOuterClass
import com.epam.esm.infrastucture.converter.proto.TagConverter
import com.epam.esm.domain.GiftCertificate
import com.epam.esm.domain.Tag
import org.springframework.stereotype.Component

@Component
class GiftCertificateConverter(
    private val dateConverter: DateConverter,
    private val tagConverter: TagConverter
) {
    fun domainToProto(
        giftCertificate: GiftCertificate
    ): GiftCertificateOuterClass.GiftCertificate {
        val tagListOfProto =
            giftCertificate.tagList
                .map { tagConverter.tagToProto(it) }
                .toList()

        return GiftCertificateOuterClass.GiftCertificate.newBuilder().apply {
            name = giftCertificate.name
            description = giftCertificate.description
            duration = giftCertificate.duration
            price = giftCertificate.price.toDouble()
            createDate = dateConverter.localDateTimeToTimestamp(giftCertificate.createDate)
            lastUpdatedDate = dateConverter.localDateTimeToTimestamp(giftCertificate.lastUpdatedDate)
            addAllTags(tagListOfProto)
        }.build()
    }

    fun protoToDomain(
        giftCertificate: GiftCertificateOuterClass.GiftCertificate
    ): GiftCertificate {
        val tagList = giftCertificate.tagsList.map { Tag(null, it.name) }

        return GiftCertificate(
            id = null,
            name = giftCertificate.name,
            description = giftCertificate.description,
            duration = giftCertificate.duration,
            createDate = dateConverter.timestampToLocalDateTime(giftCertificate.createDate),
            lastUpdatedDate = dateConverter.timestampToLocalDateTime(giftCertificate.lastUpdatedDate),
            price = giftCertificate.price.toBigDecimal(),
            tagList = tagList
        )
    }
}
