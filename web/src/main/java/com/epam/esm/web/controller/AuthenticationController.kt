package com.epam.esm.web.controller

import com.epam.esm.exception.InvalidDataException
import com.epam.esm.model.entity.User
import com.epam.esm.model.jwt.AuthenticationRequest
import com.epam.esm.service.UserService
import com.epam.esm.service.security.PersonUserDetailsService
import com.epam.esm.web.filter.JwtUtil
import com.epam.esm.web.link.UserLinkAdder
import jakarta.persistence.EntityNotFoundException
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
class AuthenticationController(
 //   private val authenticationManager: AuthenticationManager,
    private val personUserDetailsService: PersonUserDetailsService,
    private val jwtUtil: JwtUtil,
    private val userService: UserService,
    private val userLinkAdder: UserLinkAdder
) {
    @PostMapping("/authenticate")
    @ResponseStatus(HttpStatus.OK)
    fun login(@RequestBody authenticationRequest: AuthenticationRequest): String? {
        try {
           /* authenticationManager.authenticate(
                UsernamePasswordAuthenticationToken(
                    authenticationRequest.userName, authenticationRequest.password
                )
            )*/
        } catch (e: BadCredentialsException) {
            throw EntityNotFoundException("user.notfoundById")
        }
        val userDetails = personUserDetailsService.loadUserByUsername(authenticationRequest.userName)
        return jwtUtil.generateToken(userDetails)
    }

    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.CREATED)
    fun signup(
        @Valid @RequestBody user:  User,
        bindingResult: BindingResult
    ): User {
        if (bindingResult.hasErrors()) {
            throw InvalidDataException("")
        }
        userService.create(user)
        userLinkAdder.addLinks(user)
        return user
    }
}
