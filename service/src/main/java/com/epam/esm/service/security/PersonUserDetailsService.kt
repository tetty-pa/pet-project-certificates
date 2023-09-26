package com.epam.esm.service.security

import com.epam.esm.repository.UserRepository
import org.springframework.security.core.userdetails.ReactiveUserDetailsService
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class PersonUserDetailsService(private val userRepository: UserRepository) : ReactiveUserDetailsService {

    override fun findByUsername(username: String): Mono<UserDetails> {
        val user = userRepository.findByName(username)
        return user
            .switchIfEmpty(Mono.error(UsernameNotFoundException("user $username not found")))
            .map { PersonUserDetails(it) }
    }
}
