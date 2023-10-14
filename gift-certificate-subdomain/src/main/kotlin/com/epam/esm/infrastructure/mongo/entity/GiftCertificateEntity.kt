package com.epam.esm.infrastructure.mongo.entity

import com.epam.esm.domain.Tag
import com.epam.esm.entity.audit.EntityAuditListener
import jakarta.persistence.EntityListeners
import jakarta.persistence.Id
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.Size
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document
import java.math.BigDecimal
import java.time.LocalDateTime

@Document("gift_certificates")
@EntityListeners(EntityAuditListener::class)
data class GiftCertificateEntity(
    @field:Size(min = 1, max = 80, message = "gift-certificate.invalidName")
    @Indexed(unique = true)
    var name: String,

    @field: Size(min = 1, max = 250, message = "gift-certificate.invalidDescription")
    var description: String,

    @field:Min(value = 1, message = "gift-certificate.invalidPrice")
    var price: BigDecimal,

    var createDate: LocalDateTime,

    var lastUpdatedDate: LocalDateTime,

    @field:Min(value = 1, message = "gift-certificate.invalidDuration")
    var duration: Int,

    val tagList: List<Tag>
) {
    @Id
    lateinit var id: String
}
