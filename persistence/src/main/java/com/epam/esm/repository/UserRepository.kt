package com.epam.esm.repository

import com.epam.esm.model.entity.User
import org.springframework.data.mongodb.repository.MongoRepository
import java.util.Optional

interface UserRepository : MongoRepository<User, String> {
    /**
     * Gets User by  name.
     *
     * @param name Username to get
     * @return Optional<User> user if founded or Empty if not
     */
    fun findByName(name: String): Optional<User>
}

