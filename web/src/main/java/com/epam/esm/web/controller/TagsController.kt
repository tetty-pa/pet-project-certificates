package com.epam.esm.web.controller

import com.epam.esm.exception.InvalidDataException
import com.epam.esm.model.entity.Tag
import com.epam.esm.service.TagService
import com.epam.esm.web.link.TagLinkAdder
import org.springframework.http.HttpStatus
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import java.util.Objects
import javax.validation.Valid

@RestController
@RequestMapping("/tags")
class TagsController(
    private val tagService: TagService,
    private val tagLinkAdder: TagLinkAdder
) {
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    fun getAll(
        @RequestParam(value = "page", defaultValue = "0", required = false) page: Int,
        @RequestParam(value = "size", defaultValue = "25", required = false) size: Int
    ): List<Tag> {

        val all = tagService.getAll(page, size)
        all.forEach(tagLinkAdder::addLinks)
        return all
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun create(
        @Valid @RequestBody tag: Tag,
        bindingResult: BindingResult
    ): Tag {
            if (bindingResult.hasErrors()) {
                throw InvalidDataException(Objects.requireNonNull(bindingResult.fieldError).defaultMessage)
            }
        tagService.create(tag)
        tagLinkAdder.addLinks(tag)
        return tag
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    fun getById(@PathVariable("id") id: Long): Tag {
        val tag = tagService.getById(id)
        tagLinkAdder.addLinks(tag)
        return tag
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteById(@PathVariable("id") id: Long) {
        tagService.deleteById(id)
    }

    @GetMapping("/users/{userId}")
    fun getMostWidelyUsedTagOfUserWithHighestCostOfAllOrders(@PathVariable userId: Long): Tag {
        val tag = tagService.getMostWidelyUsedTagOfUserWithHighestCostOfAllOrders(userId)
        tagLinkAdder.addLinks(tag)
        return tag
    }
}
