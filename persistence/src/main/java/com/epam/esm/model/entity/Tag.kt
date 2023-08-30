package com.epam.esm.model.entity

import com.epam.esm.model.entity.audit.EntityAuditListener
import jakarta.persistence.Entity
import jakarta.persistence.EntityListeners
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import jakarta.validation.constraints.Size
import org.springframework.hateoas.RepresentationModel

@Entity
@Table(name = "tags")
@EntityListeners(EntityAuditListener::class)
data class Tag(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0,

    @field:Size(min = 1, max = 80, message = "tag.invalidName")
    var name: String = ""
) : RepresentationModel<Tag>()
