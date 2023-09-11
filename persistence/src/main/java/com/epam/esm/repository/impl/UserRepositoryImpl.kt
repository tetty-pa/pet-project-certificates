package com.epam.esm.repository.impl

import com.epam.esm.model.entity.User
import com.epam.esm.repository.UserRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.query.isEqualTo
import org.springframework.data.support.PageableExecutionUtils
import org.springframework.stereotype.Repository

@Repository
class UserRepositoryImpl(private val mongoTemplate: MongoTemplate) : UserRepository {
    override fun findAll(page: Pageable): Page<User> {
        val query = Query().with(page)
        return PageableExecutionUtils.getPage(
            mongoTemplate.find(query, User::class.java),
            page
        ) { mongoTemplate.count(query, User::class.java) }
    }

    override fun save(user: User): User =
        mongoTemplate.save(user)

    override fun findById(id: String): User? =
        mongoTemplate.findById(id, User::class.java)

    override fun findByName(name: String): User? =
        mongoTemplate.findOne<User>(Criteria("name").isEqualTo(name))
}
