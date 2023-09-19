package com.epam.esm.web.converter

import com.epam.esm.GiftCertificateOuterClass
import com.epam.esm.model.entity.GiftCertificate
import com.epam.esm.model.entity.Tag
import org.springframework.stereotype.Component

@Component
class GiftCertificateConverter(
    private val dateConverter: DateConverter,
    private val tagConverter: TagConverter
) {
    fun entityToProto(
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

    fun protoToEntity(
        giftCertificate: GiftCertificateOuterClass.GiftCertificate
    ): GiftCertificate {
        val tagList = giftCertificate.tagsList.map { Tag(it.name) }

        return GiftCertificate(
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
