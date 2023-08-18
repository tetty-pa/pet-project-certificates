package com.epam.esm.web.link

import com.epam.esm.model.entity.GiftCertificate
import com.epam.esm.model.entity.Tag
import com.epam.esm.web.controller.GiftCertificatesController
import com.epam.esm.web.controller.TagsController
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder
import org.springframework.stereotype.Component

@Component
class GiftCertificateLinkAdder : LinkAdder<GiftCertificate> {
    override fun addLinks(entity: GiftCertificate) {
        entity.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(GiftCertificatesController::class.java).getById(entity.id)).withSelfRel())
        entity.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(GiftCertificatesController::class.java).create(entity, null)).withRel("create"))
        entity.add(WebMvcLinkBuilder.linkTo(GiftCertificatesController::class.java).slash(entity.id).withRel("update"))
        entity.add(WebMvcLinkBuilder.linkTo(GiftCertificatesController::class.java).slash(entity.id).withRel("delete"))
        entity.tagList.stream()
            .filter { t: Tag -> t.links.isEmpty }
                .forEach { tag: Tag -> tag.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(TagsController::class.java).getById(tag.id)).withSelfRel()) }
    }
}
