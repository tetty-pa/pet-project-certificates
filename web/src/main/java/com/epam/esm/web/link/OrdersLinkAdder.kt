package com.epam.esm.web.link

import com.epam.esm.model.entity.Order
import com.epam.esm.web.controller.OrdersController
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder
import org.springframework.stereotype.Component


@Component
class OrdersLinkAdder : LinkAdder<Order> {
    override fun addLinks(entity: Order) {
        entity.add(WebMvcLinkBuilder.linkTo(entity.id.let {
            WebMvcLinkBuilder.methodOn(OrdersController::class.java).getById(
                it
            )
        }).withSelfRel())
        entity.add(
            WebMvcLinkBuilder.linkTo(
                WebMvcLinkBuilder.methodOn(OrdersController::class.java)
                    .create(entity.user?.id ?: 0, entity.giftCertificate?.id ?: 0)
            ).withRel("create")
        )
    }
}

