package com.epam.esm.domain

import java.math.BigDecimal
import java.time.LocalDateTime

data class Order(
    var id: String?,
    var cost: BigDecimal = BigDecimal.ZERO,
    var orderDate: LocalDateTime = LocalDateTime.now(),
    var userId: String = "",
    var giftCertificateId: String = ""
)
