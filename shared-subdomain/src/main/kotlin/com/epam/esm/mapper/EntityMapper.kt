package com.epam.esm.mapper

interface EntityMapper<D, T> {
    fun mapToDomain(entity: T): D
    fun mapToEntity(domain: D): T
}
