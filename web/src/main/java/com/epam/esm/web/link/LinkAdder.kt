package com.epam.esm.web.link

interface LinkAdder<T> {
    fun addLinks(entity: T)
}
