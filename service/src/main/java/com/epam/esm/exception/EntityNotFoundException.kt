package com.epam.esm.exception

class EntityNotFoundException : RuntimeException {
    constructor() : super()
    constructor(message: String?) : super(message)
}
