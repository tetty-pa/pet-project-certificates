package com.epam.esm.repository.impl

import org.springframework.data.mongodb.core.ReactiveMongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import reactor.core.publisher.Mono

inline fun <reified T : Any> ReactiveMongoTemplate.findOne(criteria: Criteria): Mono<T> {
    val query = Query(criteria)
    return findOne(query, T::class.java)
}
