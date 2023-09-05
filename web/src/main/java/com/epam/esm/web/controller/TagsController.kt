package com.epam.esm.web.controller

import com.epam.esm.exception.InvalidDataException
import com.epam.esm.model.entity.Tag
import com.epam.esm.service.TagService
import jakarta.validation.Valid
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

@RestController
@RequestMapping("/tags")
class TagsController(private val tagService: TagService) {
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    fun getAll(
        @RequestParam(value = "page", defaultValue = "0", required = false) page: Int,
        @RequestParam(value = "size", defaultValue = "25", required = false) size: Int
    ): List<Tag> = tagService.getAll(page, size)

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun create(
        @Valid @RequestBody tag: Tag,
        bindingResult: BindingResult?
    ): Tag {
        if (bindingResult?.hasErrors() == true) {
            throw InvalidDataException(bindingResult.fieldError?.defaultMessage ?: "")
        }
        return tagService.create(tag)
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    fun getById(@PathVariable("id") id: String): Tag =
        tagService.getById(id)

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteById(@PathVariable("id") id: String) =
        tagService.deleteById(id)

    /*  @GetMapping("/users/{userId}")
      fun getMostWidelyUsedTagOfUserWithHighestCostOfAllOrders(@PathVariable userId: String): Tag {
          val tag = tagService.getMostWidelyUsedTagOfUserWithHighestCostOfAllOrders(userId)
          tagLinkAdder.addLinks(tag)
          return tag
      }*/
}
