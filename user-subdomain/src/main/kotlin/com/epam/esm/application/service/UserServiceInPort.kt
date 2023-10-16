package com.epam.esm.application.service

import com.epam.esm.domain.User
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface UserServiceInPort {
    /**
     * Gets all Users.
     *
     * @param page page number for pagination
     * @param size   page size for pagination
     * @return List of Users Flux<User>
     */
    fun getAll(page: Int, size: Int): Flux<User>

    /**
     * Gets User by id.
     *
     * @param id User id to get
     * @return Mono<User>
     */
    fun getById(id: String): Mono<User>

    /**
     * Creates new User.
     *
     * @param user User to create
     * @return Mono<User>
     */
    fun create(user: User): Mono<User>

    /**
     * Gets User by name and password.
     *
     * @param userName User name and password to get
     * @return Mono<User>
     */
    fun login(userName: String, password: String): Mono<User>
}
