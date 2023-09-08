package com.epam.esm.repository.impl

import com.epam.esm.model.entity.GiftCertificate
import com.epam.esm.repository.GiftCertificateRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.query.isEqualTo
import org.springframework.data.support.PageableExecutionUtils
import org.springframework.stereotype.Repository

@Repository
class GiftCertificateRepositoryImpl(private val mongoTemplate: MongoTemplate) : GiftCertificateRepository {
    override fun findByName(name: String): GiftCertificate? {
        return mongoTemplate.findOne<GiftCertificate>(Criteria("name").isEqualTo(name))
    }

    override fun findAll(page: Pageable): Page<GiftCertificate> {
        val query = Query().with(page)
        return PageableExecutionUtils.getPage(
            mongoTemplate.find(query, GiftCertificate::class.java),
            page
        ) { mongoTemplate.count(query, GiftCertificate::class.java) }
    }

    override fun findById(id: String): GiftCertificate? =
        mongoTemplate.findById(id, GiftCertificate::class.java)

    override fun save(giftCertificate: GiftCertificate): GiftCertificate =
        mongoTemplate.save(giftCertificate)

    override fun deleteById(id: String) {
        val query = Query().addCriteria(Criteria.where("_id").`is`(id))
        mongoTemplate.remove(query)
    }
}
