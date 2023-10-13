package com.epam.esm.application.repository

import com.epam.esm.domain.User
import org.springframework.data.domain.Pageable
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface UserRepositoryOutPort {
    fun findAll(page: Pageable): Flux<User>

    fun save(user: User): Mono<User>

    fun findById(id: String): Mono<User>

    fun findByName(name: String): Mono<User>
}
