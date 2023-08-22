package com.epam.esm.web.controller

import com.epam.esm.exception.InvalidDataException
import com.epam.esm.model.entity.GiftCertificate
import com.epam.esm.model.entity.util.QueryParameters
import com.epam.esm.model.entity.util.SortType
import com.epam.esm.service.GiftCertificateService
import com.epam.esm.web.link.GiftCertificateLinkAdder
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.util.StringUtils
import org.springframework.validation.BindingResult
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import java.util.*
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

        val certificates = giftCertificateService.getAll(page, size);
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
            throw InvalidDataException(Objects.requireNonNull(bindingResult.fieldError).defaultMessage);
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
            throw InvalidDataException(Objects.requireNonNull(bindingResult.fieldError).defaultMessage)
        }
        giftCertificateService.update(giftCertificate)
        giftCertificateLinkAdder.addLinks(giftCertificate)
        return giftCertificate
    }

    @GetMapping("/params")
    @ResponseStatus(HttpStatus.OK)
    fun getGiftCertificatesByParameters(
        @RequestParam(name = "tag_name", required = false) tagNames: List<String>,
        @RequestParam(name = "part_of_name", required = false) partOfName: String,
        @RequestParam(name = "part_of_description", required = false) partOfDescription: String,
        @RequestParam(name = "sort_by_name", required = false) sortNameParam: String,
        @RequestParam(name = "sort_by_created_date", required = false) sortDateParam: String,
        @RequestParam(value = "page", defaultValue = "0", required = false) page: Int,
        @RequestParam(value = "size", defaultValue = "25", required = false) size: Int
    ): List<GiftCertificate> {

        var sortTypeName: SortType? = null
        if (!StringUtils.isEmpty(sortNameParam))
            sortTypeName = SortType.findSortType(sortNameParam)

        var sortTypeDate: SortType? = null
        if (!StringUtils.isEmpty(sortDateParam))
            sortTypeDate = SortType.findSortType(sortDateParam)

        val giftCertificatesByParameters = giftCertificateService
            .getGiftCertificatesByParameters(
                QueryParameters(
                    partOfName, partOfDescription,
                    tagNames, sortTypeName, sortTypeDate
                ), page, size
            )

        giftCertificatesByParameters.forEach(giftCertificateLinkAdder::addLinks)
        return giftCertificatesByParameters
    }
}
