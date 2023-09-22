package com.epam.esm.repository.impl

import com.epam.esm.model.entity.Tag
import com.epam.esm.repository.TagRepository
import com.mongodb.client.result.DeleteResult
import org.springframework.data.domain.Pageable
import org.springframework.data.mongodb.core.ReactiveMongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.query.isEqualTo
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Repository
class TagRepositoryImpl(
    private val mongoTemplate: ReactiveMongoTemplate
) : TagRepository {

    override fun findAll(page: Pageable): Flux<Tag> {
        val query = Query().with(page)
        return mongoTemplate.find(query, Tag::class.java)
    }

    override fun save(tag: Tag): Mono<Tag> =
        mongoTemplate.save(tag)

    override fun findById(id: String): Mono<Tag> =
        mongoTemplate.findById(id, Tag::class.java)

    override fun deleteById(id: String): Mono<DeleteResult> {
        val query = Query().addCriteria(Criteria.where("_id").`is`(id))
        return mongoTemplate.remove(query, Tag::class.java)
    }

    override fun findByName(name: String): Mono<Tag> =
        mongoTemplate.findOne<Tag>(Criteria("name").isEqualTo(name))
}
