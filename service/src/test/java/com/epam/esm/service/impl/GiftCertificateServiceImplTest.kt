package com.epam.esm.service.impl

import com.epam.esm.exception.DuplicateEntityException
import com.epam.esm.exception.EntityNotFoundException
import com.epam.esm.exception.InvalidDataException
import com.epam.esm.repository.GiftCertificateRepository
import com.epam.esm.service.impl.util.Constants
import com.epam.esm.service.impl.util.Constants.FIRST_TEST_GIFT_CERTIFICATE
import com.epam.esm.service.impl.util.Constants.GIFT_CERTIFICATE_TO_CREATE
import com.epam.esm.service.impl.util.Constants.INVALID_GIFT_CERTIFICATE
import com.epam.esm.service.impl.util.Constants.NOT_EXIST_ID
import com.epam.esm.service.impl.util.Constants.PAGE
import com.epam.esm.service.impl.util.Constants.PAGE_NUM
import com.epam.esm.service.impl.util.Constants.PAGE_SIZE
import com.epam.esm.service.impl.util.Constants.SECOND_TEST_GIFT_CERTIFICATE
import com.epam.esm.service.impl.util.Constants.THIRD_TEST_GIFT_CERTIFICATE
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.data.domain.PageImpl
import java.util.Optional
import org.mockito.Mockito.`when` as whenever

@ExtendWith(MockitoExtension::class)
class GiftCertificateServiceImplTest {

    @Mock
    private lateinit var giftCertificateRepository: GiftCertificateRepository

    @InjectMocks
    private lateinit var giftCertificateService: GiftCertificateServiceImpl


    @Test
    fun getAll() {
        val expected = listOf(FIRST_TEST_GIFT_CERTIFICATE, SECOND_TEST_GIFT_CERTIFICATE, THIRD_TEST_GIFT_CERTIFICATE)
        whenever(giftCertificateRepository.findAll(PAGE)).thenReturn(PageImpl(expected))
        val actual = giftCertificateService.getAll(PAGE_NUM, PAGE_SIZE)
        assertEquals(actual, expected)
    }


    @Test
    fun getById() {
        whenever(giftCertificateRepository.findById(Constants.TEST_ID)).thenReturn(
            Optional.of(
                FIRST_TEST_GIFT_CERTIFICATE
            )
        )
        val actual = giftCertificateService.getById(Constants.TEST_ID)
        assertEquals(FIRST_TEST_GIFT_CERTIFICATE, actual)
    }

    @Test
    fun getByIdShouldThrowEntityNotFoundException() {
        whenever(giftCertificateRepository.findById(NOT_EXIST_ID)).thenThrow(EntityNotFoundException(""))
        assertThrows<EntityNotFoundException> { giftCertificateService.getById(NOT_EXIST_ID) }
    }

    @Test
    fun create() {
        whenever(giftCertificateRepository.save(GIFT_CERTIFICATE_TO_CREATE)).thenReturn(GIFT_CERTIFICATE_TO_CREATE)
        whenever(GIFT_CERTIFICATE_TO_CREATE.name.let { giftCertificateRepository.findByName(it) }).thenReturn(
            null
        )
        val actual = giftCertificateService.create(GIFT_CERTIFICATE_TO_CREATE)
        assertEquals(GIFT_CERTIFICATE_TO_CREATE, actual)
    }

    @Test
    fun createShouldThrowInvalidDataException() {
        whenever(giftCertificateRepository.save(INVALID_GIFT_CERTIFICATE)).thenThrow(InvalidDataException(""))
        assertThrows<InvalidDataException> { giftCertificateService.create(INVALID_GIFT_CERTIFICATE) }
    }

    @Test
    fun createShouldThrowDuplicateEntityException() {
        whenever(giftCertificateRepository.findByName(FIRST_TEST_GIFT_CERTIFICATE.name)).thenReturn(
            FIRST_TEST_GIFT_CERTIFICATE
        )
        assertThrows<DuplicateEntityException> { giftCertificateService.create(FIRST_TEST_GIFT_CERTIFICATE) }
    }


    @Test
    fun deleteByIdShouldThrowEntityNotFoundException() {
        assertThrows<EntityNotFoundException> { giftCertificateService.deleteById(NOT_EXIST_ID) }
    }
}
