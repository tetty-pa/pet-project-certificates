package com.epam.esm.infrastucture.mongo.mapper

import com.epam.esm.domain.Tag
import com.epam.esm.infrastucture.mongo.entity.TagEntity
import com.epam.esm.mapper.EntityMapper
import org.springframework.stereotype.Component

@Component
class TagEntityMapper : EntityMapper<Tag, TagEntity> {
    override fun mapToDomain(entity: TagEntity): Tag {
        return Tag(entity.id, entity.name)
    }

    override fun mapToEntity(domain: Tag): TagEntity {
        return TagEntity(domain.name)
    }
}
