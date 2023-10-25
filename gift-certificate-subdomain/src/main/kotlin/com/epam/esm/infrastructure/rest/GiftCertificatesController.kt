package com.epam.esm.infrastructure.rest

import com.epam.esm.application.service.GiftCertificateServiceInPort
import com.epam.esm.domain.GiftCertificate
import com.epam.esm.exception.InvalidDataException
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
import org.springframework.web.bind.support.WebExchangeBindException
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/gift-certificates")
class GiftCertificatesController(
    private val giftCertificateService: GiftCertificateServiceInPort
) {
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    fun getAll(
        @RequestParam(value = "page", defaultValue = "0", required = false) page: Int,
        @RequestParam(value = "size", defaultValue = "25", required = false) size: Int
    ): Flux<GiftCertificate> {
        return giftCertificateService.getAll(page, size)
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    fun getById(@PathVariable id: String): Mono<GiftCertificate> =
        giftCertificateService.getById(id)

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun create(@Valid @RequestBody giftCertificate: GiftCertificate): Mono<GiftCertificate> {
        return giftCertificateService.create(giftCertificate)
            .onErrorMap(WebExchangeBindException::class.java) { ex ->
                val errorMessage =
                    ex.bindingResult.fieldErrors.joinToString(", ") { it.defaultMessage.toString() }
                InvalidDataException(errorMessage)
            }
    }

    @DeleteMapping(value = ["/{id}"])
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteById(@PathVariable("id") id: String): Mono<Unit> =
        giftCertificateService.deleteById(id)

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    fun update(
        @Valid @RequestBody giftCertificate: GiftCertificate,
        bindingResult: BindingResult
    ): Mono<GiftCertificate> {
        if (bindingResult.hasErrors()) {
            throw InvalidDataException(bindingResult.fieldError?.defaultMessage ?: "")
        }
        return giftCertificateService.update(giftCertificate)
    }
}
