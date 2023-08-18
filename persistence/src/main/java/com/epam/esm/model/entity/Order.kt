package com.epam.esm.model.entity

import com.epam.esm.model.entity.audit.EntityAuditListener
import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.hateoas.RepresentationModel
import java.math.BigDecimal
import java.time.ZonedDateTime
import javax.persistence.*

@Entity
@Table(name = "orders")
@EntityListeners(
    EntityAuditListener::class
)
class Order : RepresentationModel<Order> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0

    @Column(nullable = false)
    var cost: BigDecimal = BigDecimal.ZERO

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSX")
    @Column(name = "order_date", nullable = false, updatable = false)
    var orderDate: ZonedDateTime? = null

    @ManyToOne
    @JoinColumn(name = "gift_certificate_id")
    var giftCertificate: GiftCertificate? = null

    @ManyToOne
    @JoinColumn(name = "user_id")
    var user: User? = null

    constructor()
    constructor(
        id: Long,
        cost: BigDecimal,
        orderDate: ZonedDateTime?,
        giftCertificate: GiftCertificate?,
        user: User?
    ) {
        this.id = id
        this.cost = cost
        this.orderDate = orderDate
        this.giftCertificate = giftCertificate
        this.user = user
    }

    @PrePersist
    fun onCreate() {
        orderDate = ZonedDateTime.now()
    }


}
