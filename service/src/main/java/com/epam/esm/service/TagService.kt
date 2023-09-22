package com.epam.esm.service

import com.epam.esm.model.entity.Tag
import com.mongodb.client.result.DeleteResult
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface TagService {
    fun getAll(page: Int, size: Int): Flux<Tag>

    fun create(tag: Tag): Mono<Tag>

    fun getById(id: String): Mono<Tag>

    fun deleteById(id: String): Mono<DeleteResult>
}
