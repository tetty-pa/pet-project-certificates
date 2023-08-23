package com.epam.esm.web.controller

import com.epam.esm.exception.InvalidDataException
import com.epam.esm.model.entity.GiftCertificate
import com.epam.esm.service.GiftCertificateService
import com.epam.esm.web.link.GiftCertificateLinkAdder
import org.springframework.http.HttpStatus
import org.springframework.validation.BindingResult
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
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
@RequestMapping("/gift-certificates")
@Validated
class GiftCertificatesController(
    private val giftCertificateService: GiftCertificateService,
    private val giftCertificateLinkAdder: GiftCertificateLinkAdder
) {
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    fun getAll(
        @RequestParam(value = "page", defaultValue = "0", required = false) page: Int,
        @RequestParam(value = "size", defaultValue = "25", required = false) size: Int
    ): List<GiftCertificate> {

        val certificates = giftCertificateService.getAll(page, size)
        certificates.forEach(giftCertificateLinkAdder::addLinks)
        return certificates
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    fun getById(@PathVariable id: Long): GiftCertificate {
        val giftCertificate = giftCertificateService.getById(id)
        giftCertificateLinkAdder.addLinks(giftCertificate)
        return giftCertificate
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun create(
        @Valid @RequestBody giftCertificate: GiftCertificate,
        bindingResult: BindingResult
    ): GiftCertificate {

        if (bindingResult.hasErrors()) {
            throw InvalidDataException(bindingResult.fieldError.defaultMessage)
        }
        /*require(bindingResult?.hasErrors() == false) {
            bindingResult?.fieldError?.defaultMessage ?: ""
        }*/
        giftCertificateService.create(giftCertificate)
        giftCertificateLinkAdder.addLinks(giftCertificate)
        return giftCertificate
    }
       /* : ResponseEntity<GiftCertificate> {
        giftCertificateService.create(giftCertificate)
        giftCertificateLinkAdder.addLinks(giftCertificate)
        return ResponseEntity.ok(giftCertificate)*/
    /*}*/
    @DeleteMapping(value = ["/{id}"])
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteById(@PathVariable("id") id: Long) {
        giftCertificateService.deleteById(id)
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    fun update(
        @Valid @RequestBody giftCertificate: GiftCertificate,
        bindingResult: BindingResult
    ): GiftCertificate {
        if (bindingResult.hasErrors()) {
            throw InvalidDataException(bindingResult.fieldError.defaultMessage)
        }
        giftCertificateService.update(giftCertificate)
        giftCertificateLinkAdder.addLinks(giftCertificate)
        return giftCertificate
    }

}
