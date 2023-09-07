package com.epam.esm.service.security

import com.epam.esm.repository.UserRepository
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class PersonUserDetailsService(private val userRepository: UserRepository) : UserDetailsService {
    override fun loadUserByUsername(userName: String): UserDetails {
        val user = userRepository.findByName(userName)
        return PersonUserDetails(user ?: throw UsernameNotFoundException("user not found") )
    }
}
