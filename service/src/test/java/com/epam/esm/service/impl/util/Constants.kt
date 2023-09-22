package com.epam.esm.service.impl.util

import com.epam.esm.model.entity.GiftCertificate
import com.epam.esm.model.entity.Order
import com.epam.esm.model.entity.Role
import com.epam.esm.model.entity.Tag
import com.epam.esm.model.entity.User
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import java.math.BigDecimal
import java.sql.Timestamp
import java.time.LocalDateTime

object Constants {
    const val TEST_ID: String = "1"
    const val NOT_EXIST_ID: String = "100"

    @JvmField
    val FIRST_TEST_GIFT_CERTIFICATE = GiftCertificate(
        "1", "1", BigDecimal(1),
        Timestamp.valueOf("2023-01-04 12:07:19").toLocalDateTime(),
        Timestamp.valueOf("2023-01-04 12:07:19").toLocalDateTime(), 1, mutableListOf()
    )

    @JvmField
    val SECOND_TEST_GIFT_CERTIFICATE = GiftCertificate(
        "2", "2", BigDecimal(2),
        Timestamp.valueOf("2023-01-04 12:07:19").toLocalDateTime(),
        Timestamp.valueOf("2023-01-04 12:07:19").toLocalDateTime(), 2, mutableListOf()
    )

    @JvmField
    val THIRD_TEST_GIFT_CERTIFICATE = GiftCertificate(
        "3", "3", BigDecimal(3),
        Timestamp.valueOf("2023-01-04 12:07:19").toLocalDateTime(),
        Timestamp.valueOf("2023-01-04 12:07:19").toLocalDateTime(), 3, mutableListOf()
    )

    @JvmField
    val GIFT_CERTIFICATE_TO_CREATE = GiftCertificate(
        "1", "certificate new", BigDecimal("1.10"),
        Timestamp.valueOf("2023-01-04 12:07:19").toLocalDateTime(),
        Timestamp.valueOf("2023-01-04 12:07:19").toLocalDateTime(), 1, mutableListOf()
    )

    @JvmField
    val INVALID_GIFT_CERTIFICATE = GiftCertificate(
        "4", "", BigDecimal("1.10"),
        Timestamp.valueOf("2023-01-04 12:07:19").toLocalDateTime(),
        Timestamp.valueOf("2023-01-04 12:07:19").toLocalDateTime(), 1, mutableListOf()
    )

    @JvmField
    val UPDATED_GIFT_CERTIFICATE = GiftCertificate(
        "1", "new name", BigDecimal("1"),
        Timestamp.valueOf("2023-01-04 12:07:19").toLocalDateTime(),
        Timestamp.valueOf("2023-01-04 12:07:19").toLocalDateTime(), 1, mutableListOf()
    )

    @JvmField
    val FIRST_TEST_USER = User("1", "1", "1", Role.USER)

    @JvmField
    val SECOND_TEST_USER = User("2", "2", "2", Role.USER)

    @JvmField
    val USER_TO_CREATE = User("1", "1", "1", Role.USER)

    @JvmField
    val FIRST_TEST_ORDER = Order(
        BigDecimal.ONE,
        LocalDateTime.now(),
        "1",
        "1"
    )

    @JvmField
    val SECOND_TEST_ORDER = Order(
        BigDecimal(2),
        LocalDateTime.now()
    )

    @JvmField
    val FIRST_TEST_TAG = Tag("1")

    @JvmField
    val SECOND_TEST_TAG = Tag("2")

    @JvmField
    val THIRD_TEST_TAG = Tag("3")

    @JvmField
    val TAG_TO_CREATE = Tag("new")

    @JvmField
    val INVALID_TAG = Tag("")

    @JvmField
    val PAGE: Pageable = PageRequest.of(0, 25)
    const val PAGE_NUM = 0
    const val PAGE_SIZE = 25
}
