package com.epam.esm.application.security.service

import com.epam.esm.application.repository.UserRepositoryOutPort
import org.springframework.security.core.userdetails.ReactiveUserDetailsService
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class PersonUserDetailsService(private val userRepository: UserRepositoryOutPort) : ReactiveUserDetailsService {

    override fun findByUsername(username: String): Mono<UserDetails> {
        return userRepository.findByName(username)
            .switchIfEmpty(Mono.error(UsernameNotFoundException("user $username not found")))
            .map { PersonUserDetails(it) }
    }
}
