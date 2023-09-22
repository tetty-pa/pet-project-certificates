package com.epam.esm.repository.impl

import com.epam.esm.model.entity.GiftCertificate
import com.epam.esm.repository.GiftCertificateRepository
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
class GiftCertificateRepositoryImpl(
    private val mongoTemplate: ReactiveMongoTemplate
) : GiftCertificateRepository {

    override fun findByName(name: String): Mono<GiftCertificate> =
        mongoTemplate.findOne<GiftCertificate>(Criteria("name").isEqualTo(name))

    override fun findAll(page: Pageable): Flux<GiftCertificate> {
        val query = Query().with(page)
        return mongoTemplate.find(query, GiftCertificate::class.java)
    }

    override fun findById(id: String): Mono<GiftCertificate> =
        mongoTemplate.findById(id, GiftCertificate::class.java)

    override fun save(giftCertificate: GiftCertificate): Mono<GiftCertificate> =
        mongoTemplate.save(giftCertificate)

    override fun deleteById(id: String): Mono<DeleteResult> {
        val query = Query().addCriteria(Criteria.where("_id").`is`(id))
        return mongoTemplate.remove(query, GiftCertificate::class.java)
    }
}
