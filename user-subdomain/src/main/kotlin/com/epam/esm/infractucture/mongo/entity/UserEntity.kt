package com.epam.esm.infractucture.mongo.entity

import com.epam.esm.entity.audit.EntityAuditListener
import jakarta.persistence.EntityListeners
import jakarta.persistence.Id
import jakarta.validation.constraints.Size
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document

@Document("users")
@EntityListeners(EntityAuditListener::class)
data class UserEntity(
    @field:Size(min = 1, max = 80, message = "user.invalidName")
    @Indexed(unique = true)
    val name: String,

    val email: String,

    var password: String,

    val role: RoleEntity
) {
    @Id
    lateinit var id: String
}

enum class RoleEntity {
    ADMIN, USER
}
