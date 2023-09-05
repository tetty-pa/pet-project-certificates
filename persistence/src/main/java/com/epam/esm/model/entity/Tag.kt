package com.epam.esm.model.entity

import com.epam.esm.model.entity.audit.EntityAuditListener
import jakarta.persistence.EntityListeners
import jakarta.persistence.Id
import jakarta.validation.constraints.Size
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.hateoas.RepresentationModel

@Document("tags")
@EntityListeners(EntityAuditListener::class)
data class Tag(
    @field:Size(min = 1, max = 80, message = "tag.invalidName")
    val name: String
) : RepresentationModel<Tag>() {
    @Id
    lateinit var id: String
}
