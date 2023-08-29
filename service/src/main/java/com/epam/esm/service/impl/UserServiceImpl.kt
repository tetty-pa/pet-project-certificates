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
class UserServiceImpl(private val userRepository: UserRepository,
    // TODO: this code was commented because security configuration is not yet implemented;
    //  It will be fixed once security is ready
                      //private val passwordEncoder: PasswordEncoder
    ) : UserService {
    override fun getAll(page: Int, size: Int): MutableList<User> =
        userRepository.findAll(PageRequest.of(page, size)).content

    override fun getById(id: Long): User =
        userRepository.findById(id)
            .orElseThrow { EntityNotFoundException("user.notfoundById") }

    override fun create(user: User): User {
        if (userRepository.findByName(user.name).isPresent)
            throw DuplicateEntityException("user.already.exist")

    //   val encodedPassword = passwordEncoder.encode(user.password)
    //    user.password = encodedPassword
        return userRepository.save(user)
    }
}
