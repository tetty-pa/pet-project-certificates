package com.epam.esm.service

import com.epam.esm.model.entity.User

interface UserService {
    /**
     * Gets all Users.
     *
     * @param page page number for pagination
     * @param size   page size for pagination
     * @return List of Users
     */
    fun getAll(page: Int, size: Int): List<User>

    /**
     * Gets User by id.
     *
     * @param id User id to get
     * @return User
     */
    fun getById(id: Long):User

    /**
     * Creates new User.
     *
     * @param user User to create
     * @return User
     */
    fun create(user: User):User
}