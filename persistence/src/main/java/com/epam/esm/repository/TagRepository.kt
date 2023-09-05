package com.epam.esm.repository

import com.epam.esm.model.entity.Tag
import org.springframework.data.mongodb.repository.MongoRepository
import java.util.Optional

interface TagRepository : MongoRepository<Tag, String> {
    /**
     * Gets Tag by column name.
     *
     * @param name Tag column name to get
     * @return Optional<Tag> tag if founded or Empty if not
     */
    fun findByName(name: String): Optional<Tag>
    /**
     * Gets Most Widely Used Tag Of User With Highest Cost Of All Orders Tag .
     *
     * @param userId User id to get
     * @return Optional<Tag>
     */
    // TODO: this code was commented because I want to change this method later;

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
}
