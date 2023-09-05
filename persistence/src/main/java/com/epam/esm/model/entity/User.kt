package com.epam.esm.model.entity

import com.epam.esm.model.entity.audit.EntityAuditListener
import jakarta.persistence.EntityListeners
import jakarta.persistence.Id
import jakarta.validation.constraints.Size
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.hateoas.RepresentationModel

@Document("users")
@EntityListeners(EntityAuditListener::class)
data class User(
    @field:Size(min = 1, max = 80, message = "user.invalidName")
    val name: String,

    val email: String,

    var password: String,

    val role: Role
) : RepresentationModel<User>() {
    @Id
    lateinit var id: String
}

enum class Role {
    ADMIN, USER
}
