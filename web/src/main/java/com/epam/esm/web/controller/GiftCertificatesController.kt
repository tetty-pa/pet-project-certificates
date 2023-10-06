package com.epam.esm.web.controller

import com.epam.esm.GiftCertificateOuterClass.StreamAllGiftCertificatesResponse
import com.epam.esm.KafkaTopic
import com.epam.esm.exception.InvalidDataException
import com.epam.esm.model.entity.GiftCertificate
import com.epam.esm.service.GiftCertificateService
import com.epam.esm.web.converter.GiftCertificateConverter
import com.google.protobuf.GeneratedMessageV3
import com.mongodb.client.result.DeleteResult
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.kafka.core.reactive.ReactiveKafkaProducerTemplate
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
    private val giftCertificateService: GiftCertificateService,
    private val reactiveKafkaProducerTemplate: ReactiveKafkaProducerTemplate<String, GeneratedMessageV3>,
    private val giftCertificateConverter: GiftCertificateConverter
 {
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
            .flatMap {
                reactiveKafkaProducerTemplate.send(
                    KafkaTopic.ADD_GIFT_CERTIFICATE_TOPIC,
                    StreamAllGiftCertificatesResponse.newBuilder()
                        .setNewGiftCertificate(giftCertificateConverter.entityToProto(it))
                        .build()
                ).thenReturn(it)
            }
    }

    @DeleteMapping(value = ["/{id}"])
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteById(@PathVariable("id") id: String): Mono<DeleteResult> =
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
