package com.epam.esm.service.impl

import com.epam.esm.exception.DuplicateEntityException
import com.epam.esm.exception.EntityNotFoundException
import com.epam.esm.model.entity.Tag
import com.epam.esm.repository.TagRepository
import com.epam.esm.service.TagService
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service

@Service
class TagServiceImpl(private val tagRepository: TagRepository) :
    TagService {
    override fun getAll(page: Int, size: Int): List<Tag> =
        tagRepository.findAll(PageRequest.of(page, size)).content

    override fun create(tag: Tag): Tag {
        if (tagRepository.findByName(tag.name) != null)
            throw DuplicateEntityException("tag.already.exist")
        return tagRepository.save(tag)
    }

    override fun getById(id: String): Tag =
        tagRepository.findById(id) ?: throw EntityNotFoundException("tag.notfoundById")

    override fun deleteById(id: String) {
        tagRepository.findById(id) ?: throw EntityNotFoundException("tag.notfoundById")
        tagRepository.deleteById(id)
    }

}
