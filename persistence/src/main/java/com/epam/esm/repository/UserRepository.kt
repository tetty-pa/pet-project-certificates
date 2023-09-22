package com.epam.esm.repository

import com.epam.esm.model.entity.User
import org.springframework.data.domain.Pageable
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface UserRepository {
    fun findAll(page: Pageable): Flux<User>

    fun save(user: User): Mono<User>

    fun findById(id: String): Mono<User>

    fun findByName(name: String): Mono<User>
}
