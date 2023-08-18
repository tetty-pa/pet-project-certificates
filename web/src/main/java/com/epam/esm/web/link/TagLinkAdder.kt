package com.epam.esm.web.link

import com.epam.esm.model.entity.Tag
import com.epam.esm.web.controller.TagsController
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder
import org.springframework.stereotype.Component

@Component
class TagLinkAdder : LinkAdder<Tag> {
    override fun addLinks(entity: Tag) {
        entity.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(TagsController::class.java).getById(entity.id)).withSelfRel())
        entity.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(TagsController::class.java).create(entity, null)).withRel("create"))
        entity.add(WebMvcLinkBuilder.linkTo(TagsController::class.java).slash(entity.id).withRel("delete"))
    }
}
