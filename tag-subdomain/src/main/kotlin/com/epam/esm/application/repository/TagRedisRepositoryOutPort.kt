package com.epam.esm.application.repository

import com.epam.esm.domain.Tag
import com.mongodb.client.result.DeleteResult
import reactor.core.publisher.Mono

interface TagRedisRepositoryOutPort {
    fun findById(id: String): Mono<Tag>

    fun save(tag: Tag): Mono<Tag>

    fun deleteById(id: String): Mono<DeleteResult>
}
