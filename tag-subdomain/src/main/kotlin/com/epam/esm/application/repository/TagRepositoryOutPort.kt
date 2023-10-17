package com.epam.esm.application.repository

import com.epam.esm.domain.Tag
import org.springframework.data.domain.Pageable
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface TagRepositoryOutPort {
    fun findAll(page: Pageable): Flux<Tag>

    fun save(tag: Tag): Mono<Tag>

    fun findById(id: String): Mono<Tag>

    fun deleteById(id: String): Mono<Void>

    fun findByName(name: String): Mono<Tag>
}
