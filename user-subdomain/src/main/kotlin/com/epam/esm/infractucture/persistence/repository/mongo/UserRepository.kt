package com.epam.esm.repository.impl

import com.epam.esm.application.repository.UserRepositoryOutPort
import com.epam.esm.domain.User
import com.epam.esm.infractucture.persistence.entity.UserEntity
import com.epam.esm.infractucture.persistence.mapper.UserMapper
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
class UserRepository(
    private val mongoTemplate: ReactiveMongoTemplate,
    private val userMapper: UserMapper
) : UserRepositoryOutPort {

    override fun findAll(page: Pageable): Flux<User> {
        val query = Query().with(page)
        return mongoTemplate.find(query, UserEntity::class.java)
            .map { userMapper.mapToDomain(it) }
    }

    override fun save(user: User): Mono<User> =
        mongoTemplate.save(userMapper.mapToEntity(user))
            .map { userMapper.mapToDomain(it) }

    override fun findById(id: String): Mono<User> =
        mongoTemplate.findById(id, UserEntity::class.java)
            .map { userMapper.mapToDomain(it) }

    override fun findByName(name: String): Mono<User> =
        mongoTemplate.findOne<UserEntity>(Criteria("name").isEqualTo(name))
            .map { userMapper.mapToDomain(it) }
}
