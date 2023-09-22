package com.epam.esm.service

import com.epam.esm.model.entity.User
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface UserService {
    fun getAll(page: Int, size: Int): Flux<User>

    fun getById(id: String): Mono<User>

    fun create(user: User): Mono<User>
}
