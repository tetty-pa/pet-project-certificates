package com.epam.esm.infrastucture.database.mongo

import com.epam.esm.application.repository.TagRepositoryOutPort
import com.epam.esm.domain.Tag
import com.epam.esm.infrastucture.database.entity.TagEntity
import com.epam.esm.infrastucture.database.mapper.TagEntityMapper
import com.epam.esm.persistence.util.findOne
import org.springframework.data.domain.Pageable
import org.springframework.data.mongodb.core.ReactiveMongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.query.isEqualTo
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Repository
class TagRepository(
    private val mongoTemplate: ReactiveMongoTemplate,
    private val mapper: TagEntityMapper
) : TagRepositoryOutPort {

    override fun findAll(page: Pageable): Flux<Tag> {
        val query = Query().with(page)
        return mongoTemplate.find(query, TagEntity::class.java)
            .map { mapper.mapToDomain(it) }
    }

    override fun save(tag: Tag): Mono<Tag> =
        mongoTemplate.save(mapper.mapToEntity(tag))
            .map { mapper.mapToDomain(it) }

    override fun findById(id: String): Mono<Tag> =
        mongoTemplate.findById(id, TagEntity::class.java)
            .map { mapper.mapToDomain(it) }

    override fun deleteById(id: String): Mono<Void> {
        val query = Query().addCriteria(Criteria.where("_id").`is`(id))
        return mongoTemplate.remove(query, TagEntity::class.java).then()
    }

    override fun findByName(name: String): Mono<Tag> =
        mongoTemplate.findOne<TagEntity>(Criteria("name").isEqualTo(name))
            .map { mapper.mapToDomain(it) }
}
