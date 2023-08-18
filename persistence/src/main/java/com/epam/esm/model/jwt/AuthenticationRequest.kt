package com.epam.esm.model.jwt

import org.springframework.stereotype.Component

@Component
class AuthenticationRequest {
    final var userName: String? = null
    final var password: String? = null

    constructor()
    constructor(userName: String?, password: String?) {
        this.userName = userName
        this.password = password
    }
}
