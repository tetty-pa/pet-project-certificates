package com.epam.esm.exception

class InvalidParameterException : RuntimeException {
    constructor() : super()
    constructor(message: String?) : super(message)
}
