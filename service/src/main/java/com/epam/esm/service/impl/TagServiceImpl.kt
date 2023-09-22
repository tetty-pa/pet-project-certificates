package com.epam.esm.service.impl

import com.epam.esm.exception.DuplicateEntityException
import com.epam.esm.exception.EntityNotFoundException
import com.epam.esm.model.entity.Tag
import com.epam.esm.repository.TagRepository
import com.epam.esm.service.TagService
import com.mongodb.client.result.DeleteResult
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import org.yaml.snakeyaml.constructor.DuplicateKeyException
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Service
class TagServiceImpl(
    private val tagRepository: TagRepository
) :
    TagService {

    override fun getAll(page: Int, size: Int): Flux<Tag> =
        tagRepository.findAll(PageRequest.of(page, size))

    override fun create(tag: Tag): Mono<Tag> {
        return tagRepository.save(tag)
            .onErrorMap(DuplicateKeyException::class.java) {
                DuplicateEntityException("Duplicate tag error")
            }
    }

    override fun getById(id: String): Mono<Tag> {
        return tagRepository.findById(id)
            .switchIfEmpty(
                Mono.error(EntityNotFoundException("tag.notfoundById"))
            )
    }

    override fun deleteById(id: String): Mono<DeleteResult> {
        return tagRepository.findById(id)
            .switchIfEmpty(Mono.error(EntityNotFoundException("gift-certificate.notfoundById")))
            .flatMap {
                tagRepository.deleteById(id)
            }
    }
}
