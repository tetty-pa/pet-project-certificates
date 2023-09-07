package com.epam.esm.repository

import com.epam.esm.model.entity.User
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.util.Optional

interface UserRepository {
    fun findAll(page: Pageable): Page<User>

    fun save(user: User): User

    fun findById(id: String): User?

    fun findByName(name: String): User?
}

