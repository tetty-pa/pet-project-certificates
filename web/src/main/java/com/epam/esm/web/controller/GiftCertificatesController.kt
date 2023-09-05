package com.epam.esm.web.controller

import com.epam.esm.exception.InvalidDataException
import com.epam.esm.model.entity.GiftCertificate
import com.epam.esm.service.GiftCertificateService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.validation.BindingResult
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

@RestController
@RequestMapping("/gift-certificates")
class GiftCertificatesController(
    private val giftCertificateService: GiftCertificateService
) {
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    fun getAll(
        @RequestParam(value = "page", defaultValue = "0", required = false) page: Int,
        @RequestParam(value = "size", defaultValue = "25", required = false) size: Int
    ): List<GiftCertificate> = giftCertificateService.getAll(page, size)

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    fun getById(@PathVariable id: String): GiftCertificate =
        giftCertificateService.getById(id)

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun create(
        @Valid @RequestBody giftCertificate: GiftCertificate,
        bindingResult: BindingResult?
    ): GiftCertificate {
        if (bindingResult?.hasErrors() == true) {
            throw InvalidDataException(bindingResult.fieldError?.defaultMessage ?: "")
        }
        return giftCertificateService.create(giftCertificate)
    }

    @DeleteMapping(value = ["/{id}"])
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteById(@PathVariable("id") id: String) {
        giftCertificateService.deleteById(id)
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    fun update(
        @Valid @RequestBody giftCertificate: GiftCertificate,
        bindingResult: BindingResult
    ): GiftCertificate {
        if (bindingResult.hasErrors()) {
            throw InvalidDataException(bindingResult.fieldError?.defaultMessage ?: "")
        }
        return giftCertificateService.update(giftCertificate)
    }
}
