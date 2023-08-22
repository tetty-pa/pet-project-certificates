package com.epam.esm.model.jwt

import org.springframework.stereotype.Component

@Component
class AuthenticationRequest(
    val userName: String = "",
    val password: String = ""
)
