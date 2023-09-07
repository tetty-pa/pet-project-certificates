package com.epam.esm.repository.impl

import com.epam.esm.model.entity.Tag
import com.epam.esm.repository.TagRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.aggregation.Aggregation
import org.springframework.data.mongodb.core.aggregation.AggregationOperation
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.support.PageableExecutionUtils
import org.springframework.stereotype.Repository


@Repository
class TagRepositoryImpl(private val mongoTemplate: MongoTemplate) : TagRepository {
    override fun findAll(page: Pageable): Page<Tag> {
        val query = Query().with(page)
        val count: Long = mongoTemplate.count(query, Tag::class.java)
        return PageableExecutionUtils.getPage(
            mongoTemplate.find(query, Tag::class.java),
            page
        ) { count }
    }

    override fun save(tag: Tag): Tag =
        mongoTemplate.save(tag)

    override fun findById(id: String): Tag? =
        mongoTemplate.findById(id, Tag::class.java)


    override fun deleteById(id: String) {
        val query = Query().addCriteria(Criteria.where("_id").`is`(id))
        mongoTemplate.remove(query)
    }

    override fun findByName(name: String): Tag? {
        val query = Query().addCriteria(Criteria.where("name").`is`(name))
        return mongoTemplate.findOne(query, Tag::class.java)
    }

    /*    @Query(value = MOST_WIDELY_USED_WITH_HIGHEST_ORDER_COST_TAG_QUERY, nativeQuery = true)
           fun getMostWidelyUsedTagOfUserWithHighestCostOfAllOrders(userId: String): Optional<Tag>

           companion object {
               private const val MOST_WIDELY_USED_WITH_HIGHEST_ORDER_COST_TAG_QUERY = " SELECT * FROM tags" +
                       "JOIN gift_certificate_has_tag gcht on tags.id = gcht.tag_id " +
                       "JOIN orders o on gcht.gift_certificate_id = o.gift_certificate_id " +
                       "WHERE o.user_id = ? " +
                       "GROUP BY  tag_id " +
                       "ORDER BY COUNT(gcht.tag_id) DESC , SUM(o.cost) DESC LIMIT 1";
           }*/
    override fun getMostWidelyUsedTagOfUserWithHighestCostOfAllOrders(userId: String): Tag? {
        val operations: List<AggregationOperation> = listOf(
            Aggregation.match(Criteria.where("user._id").`is`(userId)),
            Aggregation.group("giftCertificate.tag._id")
                .count().`as`("tagCount")
                .sum("cost").`as`("totalCost"),
            Aggregation.sort(Sort.by(Sort.Order.desc("tagCount"), Sort.Order.desc("totalCost"))),
            Aggregation.limit(1)
        )

        val aggregation = Aggregation.newAggregation(operations)
        val results = mongoTemplate.aggregate(aggregation, "orders", Tag::class.java)
        if (results.mappedResults.isNotEmpty()) {
            val mostPopularTagId = results.mappedResults[0].id
            return mongoTemplate.findById(mostPopularTagId, Tag::class.java)
        }

        return null
    }
}
