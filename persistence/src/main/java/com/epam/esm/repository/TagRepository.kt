package com.epam.esm.repository

import com.epam.esm.model.entity.Tag
import com.mongodb.client.result.DeleteResult
import org.springframework.data.domain.Pageable
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface TagRepository {
    fun findAll(page: Pageable): Flux<Tag>

    fun save(tag: Tag): Mono<Tag>

    fun findById(id: String): Mono<Tag>

    fun deleteById(id: String): Mono<DeleteResult>

    fun findByName(name: String): Mono<Tag>
}
