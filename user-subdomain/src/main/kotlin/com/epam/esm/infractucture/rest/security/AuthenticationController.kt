package com.epam.esm.infractucture.rest.security

import com.epam.esm.application.security.model.AuthenticationRequest
import com.epam.esm.application.security.util.JwtUtil
import com.epam.esm.application.service.UserServiceInPort
import com.epam.esm.domain.User
import com.epam.esm.exception.InvalidDataException
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.support.WebExchangeBindException
import reactor.core.publisher.Mono

@RestController
class AuthenticationController(
    private val jwtUtil: JwtUtil,
    private val userService: UserServiceInPort
) {
    @PostMapping("/authenticate")
    @ResponseStatus(HttpStatus.OK)
    fun login(@RequestBody authenticationRequest: AuthenticationRequest): Mono<String> {
        return userService.login(authenticationRequest.userName, authenticationRequest.password)
            .map { jwtUtil.generateToken(it.name, it.role.name) }
    }

    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.CREATED)
    fun signup(@Valid @RequestBody user: User): Mono<User> {
        return userService.create(user)
            .onErrorMap(WebExchangeBindException::class.java) { ex ->
                val errorMessage =
                    ex.bindingResult.fieldErrors.joinToString(", ") { it.defaultMessage.toString() }
                InvalidDataException(errorMessage)
            }
    }
}
