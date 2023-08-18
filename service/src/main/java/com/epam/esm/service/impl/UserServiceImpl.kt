package com.epam.esm.service.impl

import com.epam.esm.exception.DuplicateEntityException
import com.epam.esm.exception.EntityNotFoundException
import com.epam.esm.model.entity.User
import com.epam.esm.repository.UserRepository
import com.epam.esm.service.UserService
import org.springframework.data.domain.PageRequest
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class UserServiceImpl(private val userRepository: UserRepository, private val passwordEncoder: PasswordEncoder) : UserService {
    override fun getAll(page: Int, size: Int): MutableList<User> {
        val pageRequest = PageRequest.of(page, size)
        return userRepository.findAll(pageRequest).content
    }

    override fun getById(id: Long): User {
        return userRepository.findById(id)
                .orElseThrow { EntityNotFoundException("user.notfoundById") }
    }

    override fun create(user: User): User {

        if (userRepository.findByName(user.name).isPresent) {
            throw DuplicateEntityException("user.already.exist")
        }
        val encodedPassword = passwordEncoder.encode(user?.password)
        user.password = encodedPassword
        return userRepository.save(user)
    }

}