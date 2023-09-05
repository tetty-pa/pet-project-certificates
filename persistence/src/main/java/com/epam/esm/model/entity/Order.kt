package com.epam.esm.model.entity

import com.epam.esm.model.entity.audit.EntityAuditListener
import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.persistence.EntityListeners
import jakarta.persistence.Id
import jakarta.persistence.PrePersist
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.hateoas.RepresentationModel
import java.math.BigDecimal
import java.time.LocalDateTime

@Document("orders")
@EntityListeners(EntityAuditListener::class)
data class Order(
    var cost: BigDecimal = BigDecimal.ZERO,

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSX")
    var orderDate: LocalDateTime = LocalDateTime.now()
) : RepresentationModel<Order>() {
    @Id
    lateinit var id: String

    lateinit var giftCertificate: GiftCertificate

    lateinit var user: User

    @PrePersist
    fun onCreate() {
        orderDate = LocalDateTime.now()
    }
}
