package com.epam.esm.service.impl.util;

import com.epam.esm.model.entity.*;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class Constants {


    public final static long TEST_ID = 1;
    public final static long NOT_EXIST_ID = 100;
    public final static String TEST_NAME = "1";

    public final static GiftCertificate FIRST_TEST_GIFT_CERTIFICATE =
            new GiftCertificate(1, "1", "1", new BigDecimal(1),
                    Timestamp.valueOf("2023-01-04 12:07:19").toLocalDateTime().atZone(ZoneId.of("Europe/Kiev")),
                    Timestamp.valueOf("2023-01-04 12:07:19").toLocalDateTime().atZone(ZoneId.of("Europe/Kiev")), 1);
    public final static GiftCertificate SECOND_TEST_GIFT_CERTIFICATE =
            new GiftCertificate(2, "2", "2", new BigDecimal(2),
                    Timestamp.valueOf("2023-01-04 12:07:19").toLocalDateTime().atZone(ZoneId.of("Europe/Kiev")),
                    Timestamp.valueOf("2023-01-04 12:07:19").toLocalDateTime().atZone(ZoneId.of("Europe/Kiev")), 2);

    public final static GiftCertificate THIRD_TEST_GIFT_CERTIFICATE =
            new GiftCertificate(3, "3", "3", new BigDecimal(3),
                    Timestamp.valueOf("2023-01-04 12:07:19").toLocalDateTime().atZone(ZoneId.of("Europe/Kiev")),
                    Timestamp.valueOf("2023-01-04 12:07:19").toLocalDateTime().atZone(ZoneId.of("Europe/Kiev")), 3);
    public final static GiftCertificate GIFT_CERTIFICATE_TO_CREATE = new GiftCertificate(
            1, "certificate new", "description new", new BigDecimal("1.10"),
            Timestamp.valueOf("2023-01-04 12:07:19").toLocalDateTime().atZone(ZoneId.of("Europe/Kiev")),
            Timestamp.valueOf("2023-01-04 12:07:19").toLocalDateTime().atZone(ZoneId.of("Europe/Kiev")), 1);

    public final static GiftCertificate INVALID_GIFT_CERTIFICATE = new GiftCertificate(
            4L, "", "description new", new BigDecimal("1.10"),
            Timestamp.valueOf("2023-01-04 12:07:19").toLocalDateTime().atZone(ZoneId.of("Europe/Kiev")),
            Timestamp.valueOf("2023-01-04 12:07:19").toLocalDateTime().atZone(ZoneId.of("Europe/Kiev")), 1);

    public final static GiftCertificate UPDATED_GIFT_CERTIFICATE = new GiftCertificate(
            1, "new name", "1", new BigDecimal("1"),
            Timestamp.valueOf("2023-01-04 12:07:19").toLocalDateTime().atZone(ZoneId.of("Europe/Kiev")),
            Timestamp.valueOf("2023-01-04 12:07:19").toLocalDateTime().atZone(ZoneId.of("Europe/Kiev")), 1);

    public final static User FIRST_TEST_USER =
            new User();
    public final static User SECOND_TEST_USER =
            new User();
    public final static User USER_TO_CREATE =
            new User("1", "1", "1", null);
    public final static User INVALID_USER =
            new User("", "1", "1", new Role(Role.RoleType.ADMIN.name()));

    public final static Order FIRST_TEST_ORDER =
            new Order(1, new BigDecimal(1),
                    LocalDateTime.now().atZone(ZoneId.of("Europe/Kiev")),
                    FIRST_TEST_GIFT_CERTIFICATE, FIRST_TEST_USER);

    public final static Order SECOND_TEST_ORDER =
            new Order(1, new BigDecimal(2),
                    LocalDateTime.now().atZone(ZoneId.of("Europe/Kiev")),
                    SECOND_TEST_GIFT_CERTIFICATE, FIRST_TEST_USER);

    public final static Order THIRD_TEST_ORDER =
            new Order(3, new BigDecimal(3),
                    null,
                    THIRD_TEST_GIFT_CERTIFICATE, FIRST_TEST_USER);

    public final static Tag FIRST_TEST_TAG =
            new Tag(1, "1");
    public final static Tag SECOND_TEST_TAG =
            new Tag(2, "2");
    public final static Tag THIRD_TEST_TAG =
            new Tag(3, "3");
    public final static Tag TAG_TO_CREATE = new Tag(4, "new");
    public final static Tag INVALID_TAG = new Tag(5, "");
    public final static Pageable PAGE = PageRequest.of(0, 25);
    public final static int PAGE_NUM = 0;
    public final static int PAGE_SIZE = 25;
}
