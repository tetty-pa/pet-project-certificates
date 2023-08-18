package com.epam.esm.exception

class InvalidJwtException : RuntimeException {
    constructor()
    constructor(message: String?) : super(message)
}
