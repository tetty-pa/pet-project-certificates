package com.epam.esm.infrastucture.persistence.entity

import com.epam.esm.entity.audit.EntityAuditListener
import jakarta.persistence.EntityListeners
import jakarta.persistence.Id
import jakarta.validation.constraints.Size
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document

@Document("tags")
@EntityListeners(EntityAuditListener::class)
data class TagEntity(
    @field:Size(min = 1, max = 80, message = "tag.invalidName")
    @Indexed(unique = true)
    val name: String
) {
    @Id
    lateinit var id: String
}
