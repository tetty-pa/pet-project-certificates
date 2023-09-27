package com.epam.esm.service.impl

import com.epam.esm.config.customLogger.Logging
import com.epam.esm.exception.DuplicateEntityException
import com.epam.esm.exception.EntityNotFoundException
import com.epam.esm.model.entity.User
import com.epam.esm.repository.UserRepository
import com.epam.esm.service.UserService
import org.springframework.dao.DuplicateKeyException
import org.springframework.data.domain.PageRequest
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Service
@Logging(isRequest = true)
class UserServiceImpl(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder
) : UserService {

    override fun getAll(page: Int, size: Int): Flux<User> =
        userRepository.findAll(PageRequest.of(page, size))

    override fun getById(id: String): Mono<User> {
        return userRepository
            .findById(id)
            .switchIfEmpty(Mono.error(EntityNotFoundException("user.notfoundById")))
    }

    override fun create(user: User): Mono<User> {
        val encodedPassword = passwordEncoder.encode(user.password)
        user.password = encodedPassword

        return userRepository.save(user)
            .onErrorMap(DuplicateKeyException::class.java) {
                DuplicateEntityException("Duplicate user error")
            }
    }

    override fun login(userName: String, password: String): Mono<User> {
        return Mono.fromCallable { passwordEncoder.encode(password) }
            .flatMap { encodedPassword ->
                userRepository.findByName(userName)
                    .filter { passwordEncoder.matches(password, encodedPassword) }
                    .switchIfEmpty(Mono.error(EntityNotFoundException("User with such userName and password is not found!")))
            }
    }
}
