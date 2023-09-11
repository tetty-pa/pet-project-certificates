package com.epam.esm.repository.impl

import com.epam.esm.model.entity.Tag
import com.epam.esm.repository.TagRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.query.isEqualTo
import org.springframework.data.support.PageableExecutionUtils
import org.springframework.stereotype.Repository

@Repository
class TagRepositoryImpl(private val mongoTemplate: MongoTemplate) : TagRepository {
    override fun findAll(page: Pageable): Page<Tag> {
        val query = Query().with(page)
        return PageableExecutionUtils.getPage(
            mongoTemplate.find(query, Tag::class.java),
            page
        ) { mongoTemplate.count(query, Tag::class.java) }
    }

    override fun save(tag: Tag): Tag =
        mongoTemplate.save(tag)

    override fun findById(id: String): Tag? =
        mongoTemplate.findById(id, Tag::class.java)

    override fun deleteById(id: String) {
        val query = Query().addCriteria(Criteria.where("_id").`is`(id))
        mongoTemplate.remove(query)
    }

    override fun findByName(name: String): Tag? =
        mongoTemplate.findOne<Tag>(Criteria("name").isEqualTo(name))
}
