package com.epam.esm.repository

import com.epam.esm.model.entity.Tag
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface TagRepository {
    fun findAll(page: Pageable): Page<Tag>

    fun save(tag: Tag): Tag

    fun findById(id: String): Tag?

    fun deleteById(id: String)

    fun findByName(name: String): Tag?

}
