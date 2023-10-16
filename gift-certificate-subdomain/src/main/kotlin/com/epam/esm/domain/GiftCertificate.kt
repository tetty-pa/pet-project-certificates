package com.epam.esm.domain

import java.math.BigDecimal
import java.time.LocalDateTime

data class GiftCertificate(
    var id: String?,
    var name: String,
    var description: String,
    var price: BigDecimal,
    var createDate: LocalDateTime,
    var lastUpdatedDate: LocalDateTime,
    var duration: Int,
    val tagList: List<Tag>
)
