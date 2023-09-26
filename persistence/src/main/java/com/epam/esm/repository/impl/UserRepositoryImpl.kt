package com.epam.esm.repository.impl

import com.epam.esm.model.entity.User
import com.epam.esm.repository.UserRepository
import org.springframework.data.domain.Pageable
import org.springframework.data.mongodb.core.ReactiveMongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.query.isEqualTo
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Repository
class UserRepositoryImpl(
    private val mongoTemplate: ReactiveMongoTemplate
) : UserRepository {

    override fun findAll(page: Pageable): Flux<User> {
        val query = Query().with(page)
        return mongoTemplate.find(query, User::class.java)
    }

    override fun save(user: User): Mono<User> =
        mongoTemplate.save(user)

    override fun findById(id: String): Mono<User> =
        mongoTemplate.findById(id, User::class.java)

    override fun findByName(name: String): Mono<User> =
        mongoTemplate.findOne<User>(Criteria("name").isEqualTo(name))
}
