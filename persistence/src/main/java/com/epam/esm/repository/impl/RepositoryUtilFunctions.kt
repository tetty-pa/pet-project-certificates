package com.epam.esm.repository.impl

import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query

inline fun <reified T : Any> MongoTemplate.findOne(criteria: Criteria): T? {
    val query = Query(criteria)
    return findOne(query, T::class.java)
}

