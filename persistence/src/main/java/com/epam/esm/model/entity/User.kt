package com.epam.esm.model.entity

import com.epam.esm.model.entity.audit.EntityAuditListener
import jakarta.persistence.EntityListeners
import jakarta.persistence.Id
import jakarta.validation.constraints.Size
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document

@Document("users")
@EntityListeners(EntityAuditListener::class)
data class User(
    @field:Size(min = 1, max = 80, message = "user.invalidName")
    @Indexed(unique = true)
    val name: String,

    val email: String,

    var password: String,

    val role: Role
) {
    @Id
    lateinit var id: String
}

enum class Role {
    ADMIN, USER
}
