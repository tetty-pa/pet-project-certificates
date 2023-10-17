package com.epam.esm.infrastucture.database.redis

import com.epam.esm.application.repository.TagCachingRepositoryOutPort
import com.epam.esm.domain.Tag
import com.epam.esm.exception.EntityNotFoundException
import com.epam.esm.infrastucture.database.entity.TagEntity
import com.epam.esm.infrastucture.database.mapper.TagEntityMapper
import org.springframework.data.redis.core.ReactiveRedisTemplate
import org.springframework.stereotype.Repository
import reactor.core.publisher.Mono

@Repository
class TagRedisRepository(
    private val reactiveRedisTemplate: ReactiveRedisTemplate<String, TagEntity>,
    private val mapper: TagEntityMapper
) : TagCachingRepositoryOutPort {
    override fun findById(id: String): Mono<Tag> =
        reactiveRedisTemplate.opsForValue()
            .get(id)
            .map { mapper.mapToDomain(it) }

    override fun save(tag: Tag): Mono<Tag> {
        val tagId = tag.id ?: throw EntityNotFoundException("not found entity")
        return reactiveRedisTemplate.opsForValue()
            .set(tagId, mapper.mapToEntity(tag).apply { id = tagId })
            .map { tag }
    }

    override fun deleteById(id: String): Mono<Void> =
        reactiveRedisTemplate.opsForValue()
            .delete(id)
            .then()
}
