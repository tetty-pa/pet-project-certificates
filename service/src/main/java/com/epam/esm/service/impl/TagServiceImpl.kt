package com.epam.esm.service.impl

import com.epam.esm.exception.DuplicateEntityException
import com.epam.esm.exception.EntityNotFoundException
import com.epam.esm.model.entity.Tag
import com.epam.esm.repository.TagRepository
import com.epam.esm.repository.UserRepository
import com.epam.esm.service.TagService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
open class TagServiceImpl(private val tagRepository: TagRepository, private val userRepository: UserRepository) : TagService {
    override fun getAll(page: Int, size: Int): List<Tag> {
        val pageRequest: Pageable = PageRequest.of(page, size)
        return tagRepository.findAll(pageRequest).content
    }

    @Transactional
    override fun create(tag: Tag): Tag {
        if (tagRepository.findByName(tag.name).isPresent) {
            throw DuplicateEntityException("tag.already.exist")
        }
        return tagRepository.save(tag)
    }

    override fun getById(id: Long): Tag {
        return tagRepository.findById(id)
                .orElseThrow { EntityNotFoundException("tag.notfoundById") }
    }

    @Transactional
    override fun deleteById(id: Long) {
        tagRepository.findById(id)
                .orElseThrow { EntityNotFoundException("tag.notfoundById") }
        tagRepository.deleteById(id)
    }

    override fun getMostWidelyUsedTagOfUserWithHighestCostOfAllOrders(userId: Long): Tag {
        userRepository.findById(userId)
                .orElseThrow { EntityNotFoundException("user.notfoundById") }
        return tagRepository.getMostWidelyUsedTagOfUserWithHighestCostOfAllOrders(userId)
                .orElseThrow { EntityNotFoundException("order.notfoundById") }
    }
}
