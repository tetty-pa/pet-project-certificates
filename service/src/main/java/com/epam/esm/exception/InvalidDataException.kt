package com.epam.esm.exception

class InvalidDataException : RuntimeException {
    constructor() : super()
    constructor(message: String?) : super(message)
}
