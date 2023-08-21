package com.epam.esm.service.impl.util

import com.epam.esm.model.entity.*
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import java.math.BigDecimal
import java.sql.Timestamp
import java.time.LocalDateTime
import java.time.ZoneId

object Constants {
    const val TEST_ID: Long = 1
    const val NOT_EXIST_ID: Long = 100
    const val TEST_NAME = "1"

    @JvmField
    val FIRST_TEST_GIFT_CERTIFICATE = GiftCertificate(
        1L, "1", "1", BigDecimal(1),
        Timestamp.valueOf("2023-01-04 12:07:19").toLocalDateTime().atZone(ZoneId.of("Europe/Kiev")),
        Timestamp.valueOf("2023-01-04 12:07:19").toLocalDateTime().atZone(ZoneId.of("Europe/Kiev")), 1
    )
    @JvmField
    val SECOND_TEST_GIFT_CERTIFICATE = GiftCertificate(
        2, "2", "2", BigDecimal(2),
        Timestamp.valueOf("2023-01-04 12:07:19").toLocalDateTime().atZone(ZoneId.of("Europe/Kiev")),
        Timestamp.valueOf("2023-01-04 12:07:19").toLocalDateTime().atZone(ZoneId.of("Europe/Kiev")), 2
    )
    @JvmField
    val THIRD_TEST_GIFT_CERTIFICATE = GiftCertificate(
        3, "3", "3", BigDecimal(3),
        Timestamp.valueOf("2023-01-04 12:07:19").toLocalDateTime().atZone(ZoneId.of("Europe/Kiev")),
        Timestamp.valueOf("2023-01-04 12:07:19").toLocalDateTime().atZone(ZoneId.of("Europe/Kiev")), 3
    )
    @JvmField
    val GIFT_CERTIFICATE_TO_CREATE = GiftCertificate(
        1, "certificate new", "description new", BigDecimal("1.10"),
        Timestamp.valueOf("2023-01-04 12:07:19").toLocalDateTime().atZone(ZoneId.of("Europe/Kiev")),
        Timestamp.valueOf("2023-01-04 12:07:19").toLocalDateTime().atZone(ZoneId.of("Europe/Kiev")), 1
    )
    @JvmField
    val INVALID_GIFT_CERTIFICATE = GiftCertificate(
        4L, "", "description new", BigDecimal("1.10"),
        Timestamp.valueOf("2023-01-04 12:07:19").toLocalDateTime().atZone(ZoneId.of("Europe/Kiev")),
        Timestamp.valueOf("2023-01-04 12:07:19").toLocalDateTime().atZone(ZoneId.of("Europe/Kiev")), 1
    )
    @JvmField
    val UPDATED_GIFT_CERTIFICATE = GiftCertificate(
        1, "new name", "1", BigDecimal("1"),
        Timestamp.valueOf("2023-01-04 12:07:19").toLocalDateTime().atZone(ZoneId.of("Europe/Kiev")),
        Timestamp.valueOf("2023-01-04 12:07:19").toLocalDateTime().atZone(ZoneId.of("Europe/Kiev")), 1
    )
    @JvmField
    val FIRST_TEST_USER = User(1, "1", "1", "1", mutableListOf())
    @JvmField
    val SECOND_TEST_USER = User()
    @JvmField
    val USER_TO_CREATE = User(1, "1", "1", "1", mutableListOf())
    val INVALID_USER = User(1, "", "1", "1", mutableListOf(), Role(1, Role.RoleType.ADMIN.name))
    @JvmField
    val FIRST_TEST_ORDER = Order(
        1, BigDecimal(1),
        LocalDateTime.now().atZone(ZoneId.of("Europe/Kiev")),
        FIRST_TEST_GIFT_CERTIFICATE, FIRST_TEST_USER
    )
    @JvmField
    val SECOND_TEST_ORDER = Order(
        1, BigDecimal(2),
        LocalDateTime.now().atZone(ZoneId.of("Europe/Kiev")),
        SECOND_TEST_GIFT_CERTIFICATE, FIRST_TEST_USER
    )
    @JvmField
    val THIRD_TEST_ORDER = Order(
        3, BigDecimal(3),
        null,
        THIRD_TEST_GIFT_CERTIFICATE, FIRST_TEST_USER
    )
    @JvmField
    val FIRST_TEST_TAG = Tag(1, "1")
    @JvmField
    val SECOND_TEST_TAG = Tag(2, "2")
    @JvmField
    val THIRD_TEST_TAG = Tag(3, "3")
    @JvmField
    val TAG_TO_CREATE = Tag(4, "new")
    @JvmField
    val INVALID_TAG = Tag(5, "")
    @JvmField
    val PAGE: Pageable = PageRequest.of(0, 25)
    const val PAGE_NUM = 0
    const val PAGE_SIZE = 25
}
