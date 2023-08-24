package com.epam.esm.repository

import com.epam.esm.model.entity.User
import org.springframework.data.jpa.repository.JpaRepository
import java.util.Optional

interface UserRepository : JpaRepository<User, Long> {
    /**
     * Gets User by  name.
     *
     * @param name Username to get
     * @return Optional<User> user if founded or Empty if not
     */
    fun findByName(name: String): Optional<User>
}

