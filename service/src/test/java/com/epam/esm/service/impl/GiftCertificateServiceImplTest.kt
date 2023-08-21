package com.epam.esm.service.impl

import com.epam.esm.exception.DuplicateEntityException
import com.epam.esm.exception.EntityNotFoundException
import com.epam.esm.exception.InvalidDataException
import com.epam.esm.model.entity.GiftCertificate
import com.epam.esm.model.entity.util.QueryParameters
import com.epam.esm.repository.GiftCertificateRepository
import com.epam.esm.repository.TagRepository
import com.epam.esm.service.impl.util.Constants
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.data.domain.PageImpl
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.assertThrows
import java.util.*
import org.mockito.Mockito.`when` as whenever

@ExtendWith(MockitoExtension::class)
class GiftCertificateServiceImplTest {

    @Mock
    private lateinit var giftCertificateRepository: GiftCertificateRepository

    @Mock
    private lateinit var tagRepository: TagRepository

    @InjectMocks
    private lateinit var giftCertificateService: GiftCertificateServiceImpl

    @Test
    fun getAll() {
        val expected = listOf(Constants.FIRST_TEST_GIFT_CERTIFICATE, Constants.SECOND_TEST_GIFT_CERTIFICATE, Constants.THIRD_TEST_GIFT_CERTIFICATE)
        whenever(giftCertificateRepository.findAll(Constants.PAGE)).thenReturn(PageImpl(expected))
        val actual = giftCertificateService.getAll(Constants.PAGE_NUM, Constants.PAGE_SIZE)
        assertEquals(actual, expected)
    }

    @Test
    fun getGiftCertificatesByParameters() {
        val parameters = QueryParameters("1", "", emptyList(), null, null)
        val actual = giftCertificateService.getGiftCertificatesByParameters(parameters, Constants.PAGE_NUM, Constants.PAGE_SIZE)
        assertEquals(emptyList<GiftCertificate>(), actual)
    }

    @Test
    fun getById() {
        whenever(giftCertificateRepository.findById(Constants.TEST_ID)).thenReturn(Optional.of(Constants.FIRST_TEST_GIFT_CERTIFICATE))
        val actual = giftCertificateService.getById(Constants.TEST_ID)
        assertEquals(Constants.FIRST_TEST_GIFT_CERTIFICATE, actual)
    }

    @Test
    fun getByIdShouldThrowEntityNotFoundException() {
        whenever(giftCertificateRepository.findById(Constants.NOT_EXIST_ID)).thenThrow(EntityNotFoundException())
        assertThrows<EntityNotFoundException> { giftCertificateService.getById(Constants.NOT_EXIST_ID) }
    }

    @Test
    fun create() {
        whenever(giftCertificateRepository.save(Constants.GIFT_CERTIFICATE_TO_CREATE)).thenReturn(Constants.GIFT_CERTIFICATE_TO_CREATE)
        whenever(Constants.GIFT_CERTIFICATE_TO_CREATE.name?.let { giftCertificateRepository.findByName(it) }).thenReturn(
                Optional.empty(),
                Optional.of(Constants.GIFT_CERTIFICATE_TO_CREATE)
        )
        val actual = giftCertificateService.create(Constants.GIFT_CERTIFICATE_TO_CREATE)
        assertEquals(Constants.GIFT_CERTIFICATE_TO_CREATE, actual)
    }

    @Test
    fun createShouldThrowInvalidDataException() {
        whenever(giftCertificateRepository.save(Constants.INVALID_GIFT_CERTIFICATE)).thenThrow(InvalidDataException())
        assertThrows<InvalidDataException> { giftCertificateService.create(Constants.INVALID_GIFT_CERTIFICATE) }
    }

    @Test
    fun createShouldThrowDuplicateEntityException() {
        whenever(Constants.FIRST_TEST_GIFT_CERTIFICATE.name?.let { giftCertificateRepository.findByName(it) }).thenReturn(Optional.of(Constants.FIRST_TEST_GIFT_CERTIFICATE))
        assertThrows<DuplicateEntityException> { giftCertificateService.create(Constants.FIRST_TEST_GIFT_CERTIFICATE) }
    }

    @Test
    fun update() {
        whenever(giftCertificateRepository.save(Constants.UPDATED_GIFT_CERTIFICATE)).thenReturn(Constants.UPDATED_GIFT_CERTIFICATE)
        whenever(giftCertificateRepository.findById(Constants.UPDATED_GIFT_CERTIFICATE.id)).thenReturn(Optional.of(Constants.FIRST_TEST_GIFT_CERTIFICATE))
        val actual = giftCertificateService.update(Constants.UPDATED_GIFT_CERTIFICATE)
        assertEquals(Constants.UPDATED_GIFT_CERTIFICATE, actual)
    }

    @Test
    fun updateShouldThrowInvalidDataException() {
        whenever(giftCertificateRepository.findById(Constants.INVALID_GIFT_CERTIFICATE.id)).thenReturn(Optional.of(Constants.INVALID_GIFT_CERTIFICATE))
        whenever(giftCertificateRepository.save(Constants.INVALID_GIFT_CERTIFICATE)).thenThrow(InvalidDataException())
        assertThrows<InvalidDataException> { giftCertificateService.update(Constants.INVALID_GIFT_CERTIFICATE) }
    }

    @Test
    fun deleteByIdShouldThrowEntityNotFoundException() {
        assertThrows<EntityNotFoundException> { giftCertificateService.deleteById(Constants.NOT_EXIST_ID) }
    }
}
