package com.epam.esm.service.impl;

import com.epam.esm.exception.DuplicateEntityException;
import com.epam.esm.exception.EntityNotFoundException;
import com.epam.esm.exception.InvalidDataException;
import com.epam.esm.model.entity.GiftCertificate;
import com.epam.esm.model.entity.util.QueryParameters;
import com.epam.esm.repository.GiftCertificateRepository;
import com.epam.esm.repository.TagRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.epam.esm.service.impl.util.Constants.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GiftCertificateServiceImplTest {

    @Mock
    private GiftCertificateRepository giftCertificateRepository;

    @Mock
    private TagRepository tagRepository;

    @InjectMocks
    private GiftCertificateServiceImpl giftCertificateService;

    @Test
    void getAll() {
        List<GiftCertificate> expected = Arrays.asList(FIRST_TEST_GIFT_CERTIFICATE, SECOND_TEST_GIFT_CERTIFICATE, THIRD_TEST_GIFT_CERTIFICATE);
        when(giftCertificateRepository.findAll(PAGE)).thenReturn(new PageImpl<>(expected));
        List<GiftCertificate> actual = giftCertificateService.getAll(PAGE_NUM, PAGE_SIZE);

        assertEquals(actual, expected);
    }

    @Test
    void getGiftCertificatesByParameters() {
        QueryParameters parameters = new QueryParameters("1", "", new ArrayList<>(), null, null);
        List<GiftCertificate> actual = giftCertificateService.getGiftCertificatesByParameters(parameters, PAGE_NUM, PAGE_SIZE);

        assertEquals(new ArrayList<>(), actual);
    }


    @Test
    void getById() {
        when(giftCertificateRepository.findById(TEST_ID)).thenReturn(Optional.of(FIRST_TEST_GIFT_CERTIFICATE));
        GiftCertificate actual = giftCertificateService.getById(TEST_ID);
        assertEquals(FIRST_TEST_GIFT_CERTIFICATE, actual);
    }

    @Test
    void getByIdShouldThrowEntityNotFoundException() {
        when(giftCertificateRepository.findById(NOT_EXIST_ID)).thenThrow(new EntityNotFoundException());
        assertThrows(EntityNotFoundException.class, () -> giftCertificateService.getById(NOT_EXIST_ID));
    }


    @Test
    void create() {
        when(giftCertificateRepository.save(GIFT_CERTIFICATE_TO_CREATE)).thenReturn(GIFT_CERTIFICATE_TO_CREATE);
        when(giftCertificateRepository.findByName(GIFT_CERTIFICATE_TO_CREATE.getName())).thenReturn(Optional.empty())
                .thenReturn(Optional.of(GIFT_CERTIFICATE_TO_CREATE));
        GiftCertificate actual = giftCertificateService.create(GIFT_CERTIFICATE_TO_CREATE);
        assertEquals(GIFT_CERTIFICATE_TO_CREATE, actual);
    }

    @Test
    void createShouldThrowInvalidDataException() {
        when(giftCertificateRepository.save(INVALID_GIFT_CERTIFICATE)).thenThrow(new InvalidDataException());
        assertThrows(InvalidDataException.class, () -> giftCertificateService.create(INVALID_GIFT_CERTIFICATE));
    }


    @Test
    void createShouldThrowDuplicateEntityException() {
        when(giftCertificateRepository.findByName(FIRST_TEST_GIFT_CERTIFICATE.getName())).thenReturn(Optional.of(FIRST_TEST_GIFT_CERTIFICATE));
        assertThrows(DuplicateEntityException.class, () -> giftCertificateService.create(FIRST_TEST_GIFT_CERTIFICATE));
    }

    @Test
    void update() {
        when(giftCertificateRepository.save(UPDATED_GIFT_CERTIFICATE)).thenReturn(UPDATED_GIFT_CERTIFICATE);
        when(giftCertificateRepository.findById(UPDATED_GIFT_CERTIFICATE.getId())).thenReturn(Optional.of(FIRST_TEST_GIFT_CERTIFICATE));
        GiftCertificate actual = giftCertificateService.update(UPDATED_GIFT_CERTIFICATE);
        assertEquals(UPDATED_GIFT_CERTIFICATE, actual);
    }

    @Test
    void updateShouldThrowInvalidDataException() {
        when(giftCertificateRepository.findById(INVALID_GIFT_CERTIFICATE.getId())).thenReturn(Optional.of(INVALID_GIFT_CERTIFICATE));
        when(giftCertificateRepository.save(INVALID_GIFT_CERTIFICATE)).thenThrow(new InvalidDataException());
        assertThrows(InvalidDataException.class, () -> giftCertificateService.update(UPDATED_GIFT_CERTIFICATE));
    }


    @Test
    void deleteByIdShouldThrowEntityNotFoundException() {
        assertThrows(EntityNotFoundException.class, () -> giftCertificateService.deleteById(NOT_EXIST_ID));
    }
}