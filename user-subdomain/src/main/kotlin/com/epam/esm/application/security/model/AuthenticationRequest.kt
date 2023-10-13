package com.epam.esm.application.security.model

import org.springframework.stereotype.Component

@Component
class AuthenticationRequest(
        val userName: String = "",
        val password: String = ""
)
