package com.epam.esm.service

import com.epam.esm.model.entity.Tag

interface TagService {
    /**
     * Gets all Tags.
     *
     * @param page page number for pagination
     * @param size   page size for pagination
     * @return List of Tags
     */
    fun getAll(page: Int, size: Int): List<Tag>

    /**
     * Creates new Tag.
     *
     * @param tag Tag to create
     * @return Tag
     */
    fun create(tag: Tag): Tag

    /**
     * Gets Tag by id.
     *
     * @param id Tag id to get
     * @return Tag
     */
    fun getById(id: Long): Tag

    /**
     * Deletes Gift Certificates.
     *
     * @param id Tag id to delete
     */
    fun deleteById(id: Long)

    /**
     * Gets Most Widely Used Tag Of User With Highest Cost Of All Orders Tag .
     *
     * @param userId User id to get
     * @return Tag
     */
    fun getMostWidelyUsedTagOfUserWithHighestCostOfAllOrders(userId: Long): Tag
}
