package com.epam.esm.infrastucture.converter.proto

import com.epam.esm.TagOuterClass
import com.epam.esm.domain.Tag
import org.springframework.stereotype.Component

@Component
class TagConverter {
    fun tagToProto(tag: Tag): TagOuterClass.Tag {
        return TagOuterClass.Tag.newBuilder().apply {
            name = tag.name
        }.build()
    }
}
