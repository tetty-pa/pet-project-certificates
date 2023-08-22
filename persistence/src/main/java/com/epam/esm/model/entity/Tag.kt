package com.epam.esm.model.entity

import com.epam.esm.model.entity.audit.EntityAuditListener
import org.springframework.hateoas.RepresentationModel

import javax.persistence.*
import javax.validation.constraints.Size

@Entity
@Table(name = "tags")
@EntityListeners(
    EntityAuditListener::class
)
data class Tag(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0,

    var name: @Size(min = 1, max = 80, message = "tag.invalidName") String = ""
) : RepresentationModel<Tag>()
