package com.epam.esm.application.service

import com.epam.esm.application.repository.TagCachingRepositoryOutPort
import com.epam.esm.application.repository.TagRepositoryOutPort
import com.epam.esm.domain.Tag
import com.epam.esm.exception.DuplicateEntityException
import com.epam.esm.exception.EntityNotFoundException
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import org.yaml.snakeyaml.constructor.DuplicateKeyException
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Service
class TagService(
    private val tagRepository: TagRepositoryOutPort,
    private val redisRepository: TagCachingRepositoryOutPort,
) : TagServiceInPort {

    override fun getAll(page: Int, size: Int): Flux<Tag> =
        tagRepository.findAll(PageRequest.of(page, size))

    override fun create(tag: Tag): Mono<Tag> {
        return tagRepository.save(tag)
            .onErrorMap(DuplicateKeyException::class.java) {
                DuplicateEntityException("Duplicate tag error")
            }
    }

    override fun getById(id: String): Mono<Tag> {
        return redisRepository.findById(id)
            .switchIfEmpty(
                tagRepository.findById(id)
                    .flatMap { redisRepository.save(it) }
            ).switchIfEmpty(Mono.error(EntityNotFoundException("tag.notfoundById")))
    }

    override fun deleteById(id: String): Mono<Unit> {
        return tagRepository.findById(id)
            .switchIfEmpty(Mono.error(EntityNotFoundException("gift-certificate.notfoundById")))
            .flatMap { tagRepository.deleteById(id) }
            .then(redisRepository.deleteById(id))

    }
}
