package com.epam.esm.web.link

import com.epam.esm.model.entity.User
import com.epam.esm.web.controller.UsersController
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder
import org.springframework.stereotype.Component

@Component
class UserLinkAdder : LinkAdder<User> {
    override fun addLinks(entity: User) {
        entity.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UsersController::class.java).getById(entity.id)).withSelfRel())
    }
}
