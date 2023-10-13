package com.epam.esm.domain

data class User(
    val id: String?,
    val name: String,
    val email: String,
    var password: String,
    val role: Role
)

enum class Role {
    ADMIN, USER
}
