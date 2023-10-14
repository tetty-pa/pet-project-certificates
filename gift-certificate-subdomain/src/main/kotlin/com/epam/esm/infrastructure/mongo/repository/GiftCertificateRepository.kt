package com.epam.esm.infrastructure.mongo.repository

import com.epam.esm.application.repository.GiftCertificateRepositoryOutPort
import com.epam.esm.domain.GiftCertificate
import com.epam.esm.infrastructure.mongo.entity.GiftCertificateEntity
import com.epam.esm.infrastructure.mongo.mapper.GiftCertificateMapper
import com.epam.esm.persistence.util.findOne
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
class GiftCertificateRepository(
    private val mongoTemplate: ReactiveMongoTemplate,
    private val giftCertificateMapper: GiftCertificateMapper
) : GiftCertificateRepositoryOutPort {

    override fun findByName(name: String): Mono<GiftCertificate> =
        mongoTemplate.findOne<GiftCertificateEntity>(Criteria("name").isEqualTo(name))
            .map { giftCertificateMapper.mapToDomain(it) }

    override fun findAll(page: Pageable): Flux<GiftCertificate> {
        val query = Query().with(page)
        return mongoTemplate.find(query, GiftCertificateEntity::class.java)
            .map { giftCertificateMapper.mapToDomain(it) }
    }

    override fun findById(id: String): Mono<GiftCertificate> {
        return mongoTemplate.findById(id, GiftCertificateEntity::class.java)
            .map { giftCertificateMapper.mapToDomain(it) }
    }

    override fun save(giftCertificate: GiftCertificate): Mono<GiftCertificate> =
        mongoTemplate.save(giftCertificateMapper.mapToEntity(giftCertificate))
            .map { giftCertificateMapper.mapToDomain(it) }


    override fun deleteById(id: String): Mono<DeleteResult> {
        val query = Query().addCriteria(Criteria.where("_id").`is`(id))
        return mongoTemplate.remove(query, GiftCertificateEntity::class.java)
    }
}
