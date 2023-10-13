package com.epam.esm.infrastucture.persistence.entity

import com.epam.esm.entity.audit.EntityAuditListener
import jakarta.persistence.EntityListeners
import jakarta.persistence.Id
import jakarta.persistence.PrePersist
import org.springframework.data.mongodb.core.mapping.Document
import java.math.BigDecimal
import java.time.LocalDateTime

@Document("orders")
@EntityListeners(EntityAuditListener::class)
data class OrderEntity(
    var cost: BigDecimal = BigDecimal.ZERO,

    var orderDate: LocalDateTime = LocalDateTime.now(),

    var userId: String = "",

    var giftCertificateId: String = ""
) {
    @Id
    lateinit var id: String

    @PrePersist
    fun onCreate() {
        orderDate = LocalDateTime.now()
    }
}
