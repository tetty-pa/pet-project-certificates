package com.epam.esm.infrastucture.rest

import com.epam.esm.application.service.TagService
import com.epam.esm.domain.Tag
import com.epam.esm.exception.InvalidDataException
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.support.WebExchangeBindException
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/tags")
class TagsController(private val tagService: TagService) {
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    fun getAll(
        @RequestParam(value = "page", defaultValue = "0", required = false) page: Int,
        @RequestParam(value = "size", defaultValue = "25", required = false) size: Int
    ): Flux<Tag> = tagService.getAll(page, size)

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun create(@Valid @RequestBody tag: Tag): Mono<Tag> {
        return tagService.create(tag)
            .onErrorMap(WebExchangeBindException::class.java) { ex ->
                val errorMessage =
                    ex.bindingResult.fieldErrors.joinToString(", ") { it.defaultMessage.toString() }
                InvalidDataException(errorMessage)
            }
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    fun getById(@PathVariable("id") id: String): Mono<Tag> =
        tagService.getById(id)

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteById(@PathVariable("id") id: String): Mono<Unit> =
        tagService.deleteById(id).thenReturn(Unit)
}
