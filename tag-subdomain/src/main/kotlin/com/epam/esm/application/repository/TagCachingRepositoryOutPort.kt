package com.epam.esm.application.repository

import com.epam.esm.domain.Tag
import reactor.core.publisher.Mono

interface TagCachingRepositoryOutPort {
    fun findById(id: String): Mono<Tag>

    fun save(tag: Tag): Mono<Tag>

    fun deleteById(id: String): Mono<Unit>
}
