package com.epam.esm.application.service

import com.epam.esm.domain.Tag
import com.mongodb.client.result.DeleteResult
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface TagServiceInPort {
    /**
     * Gets all Tags.
     *
     * @param page page number for pagination
     * @param size   page size for pagination
     * @return List of Tags Flux<Tag>
     */
    fun getAll(page: Int, size: Int): Flux<Tag>

    /**
     * Creates new Tag.
     *
     * @param tag Tag to create
     * @return created Mono<Tag>
     */
    fun create(tag: Tag): Mono<Tag>

    /**
     * Gets Tag by id.
     *
     * @param id Tag id to get
     * @return Mono<Tag>
     */
    fun getById(id: String): Mono<Tag>

    /**
     * Deletes Tag
     *
     * @param id Tag id to delete
     * @return Mono<DeleteResult>
     */
    fun deleteById(id: String): Mono<DeleteResult>
}
