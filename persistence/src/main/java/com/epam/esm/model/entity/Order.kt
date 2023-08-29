package com.epam.esm.model.entity

import com.epam.esm.model.entity.audit.EntityAuditListener
import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EntityListeners
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.PrePersist
import jakarta.persistence.Table
import org.springframework.hateoas.RepresentationModel
import java.math.BigDecimal
import java.time.ZonedDateTime

@Entity
@Table(name = "orders")
@EntityListeners(EntityAuditListener::class)
data class Order(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0,

    var cost: BigDecimal = BigDecimal.ZERO,

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSX")
    @Column(name = "order_date", nullable = false, updatable = false)
    var orderDate: ZonedDateTime? = null,

    @ManyToOne
    @JoinColumn(name = "gift_certificate_id")
    var giftCertificate: GiftCertificate? = null,

    @ManyToOne
    @JoinColumn(name = "user_id")
    var user: User? = null
) : RepresentationModel<Order>() {

    @PrePersist
    fun onCreate() {
        orderDate = ZonedDateTime.now()
    }
}
