package com.epam.esm.repository.impl

import com.epam.esm.model.entity.GiftCertificate
import com.epam.esm.repository.GiftCertificateRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.support.PageableExecutionUtils
import org.springframework.stereotype.Repository

@Repository
class GiftCertificateRepositoryImpl(private val mongoTemplate: MongoTemplate) : GiftCertificateRepository {
    override fun findByName(name: String): GiftCertificate? {
        val query = Query().addCriteria(Criteria.where("_name").`is`(name))
        return mongoTemplate.findOne(query, GiftCertificate::class.java)
    }

    override fun findAll(page: Pageable): Page<GiftCertificate> {
        val query = Query().with(page)
        val count: Long = mongoTemplate.count(query, GiftCertificate::class.java)
        return PageableExecutionUtils.getPage(
            mongoTemplate.find(query, GiftCertificate::class.java),
            page
        ) { count }
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
