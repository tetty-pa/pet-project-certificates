package com.epam.esm.web.converter

import com.epam.esm.GiftCertificateOuterClass
import com.epam.esm.TagList
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
            giftCertificate.tagList.map { tag ->
                tagConverter.tagToProto(tag)
            }.toList()
        val listOfTags = TagList.ListOfTags.newBuilder().addAllTags(tagListOfProto)

        return GiftCertificateOuterClass.GiftCertificate.newBuilder()
            .setName(giftCertificate.name)
            .setDescription(giftCertificate.description)
            .setDuration(giftCertificate.duration)
            .setPrice(giftCertificate.price.toDouble())
            .setCreateDate(
                dateConverter
                    .localDateTimeToTimestamp(giftCertificate.createDate)
            )
            .setLastUpdatedDate(
                dateConverter
                    .localDateTimeToTimestamp(giftCertificate.lastUpdatedDate)
            )
            .setTagList(listOfTags)
            .build()
    }

    fun protoToEntity(
        giftCertificate: GiftCertificateOuterClass.GiftCertificate
    ): GiftCertificate {
        val tagList = giftCertificate.tagList.tagsList.map { tag -> Tag(tag.name) }

        return GiftCertificate(
            name = giftCertificate.name,
            description = giftCertificate.description,
            duration = giftCertificate.duration,
            createDate = dateConverter
                .timestampToLocalDateTime(giftCertificate.createDate),
            lastUpdatedDate = dateConverter
                .timestampToLocalDateTime(giftCertificate.lastUpdatedDate),
            price = giftCertificate.price.toBigDecimal(),
            tagList = tagList
        )
    }

}
