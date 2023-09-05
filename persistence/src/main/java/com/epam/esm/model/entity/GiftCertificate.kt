package com.epam.esm.model.entity

import com.epam.esm.model.entity.audit.EntityAuditListener
import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.persistence.EntityListeners
import jakarta.persistence.Id
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.Size
import org.springframework.data.mongodb.core.mapping.Document
import java.math.BigDecimal
import java.time.LocalDateTime


@Document("gift_certificates")
@EntityListeners(EntityAuditListener::class)
data class GiftCertificate(
    @field:Size(min = 1, max = 80, message = "gift-certificate.invalidName")
    var name: String,

    @field: Size(min = 1, max = 250, message = "gift-certificate.invalidDescription")
    var description: String,

    @field:Min(value = 1, message = "gift-certificate.invalidPrice")
    var price: BigDecimal,

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSX")
    var createDate: LocalDateTime,

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSX")
    var lastUpdatedDate: LocalDateTime,

    @field:Min(value = 1, message = "gift-certificate.invalidDuration")
    var duration: Int,

    val tagList: List<Tag>
) {
    @Id
    lateinit var id: String
}
