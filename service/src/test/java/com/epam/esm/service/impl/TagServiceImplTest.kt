package com.epam.esm.service.impl

import com.epam.esm.exception.DuplicateEntityException
import com.epam.esm.exception.EntityNotFoundException
import com.epam.esm.exception.InvalidDataException
import com.epam.esm.repository.TagRepository
import com.epam.esm.repository.UserRepository
import com.epam.esm.service.impl.util.Constants.FIRST_TEST_TAG
import com.epam.esm.service.impl.util.Constants.FIRST_TEST_USER
import com.epam.esm.service.impl.util.Constants.INVALID_TAG
import com.epam.esm.service.impl.util.Constants.NOT_EXIST_ID
import com.epam.esm.service.impl.util.Constants.PAGE
import com.epam.esm.service.impl.util.Constants.PAGE_NUM
import com.epam.esm.service.impl.util.Constants.PAGE_SIZE
import com.epam.esm.service.impl.util.Constants.SECOND_TEST_TAG
import com.epam.esm.service.impl.util.Constants.TAG_TO_CREATE
import com.epam.esm.service.impl.util.Constants.TEST_ID
import com.epam.esm.service.impl.util.Constants.THIRD_TEST_TAG
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
class TagServiceImplTest {

    @Mock
    private lateinit var tagRepository: TagRepository

    @Mock
    private lateinit var userRepository: UserRepository

    @InjectMocks
    private lateinit var tagService: TagServiceImpl

    @Test
    fun getAll() {
        val tags = listOf(FIRST_TEST_TAG, SECOND_TEST_TAG, THIRD_TEST_TAG)
        whenever(tagRepository.findAll(PAGE)).thenReturn(PageImpl(tags))
        val actual = tagService.getAll(PAGE_NUM, PAGE_SIZE)
        assertEquals(tags, actual)
    }

    @Test
    fun create() {
        whenever(tagRepository.save(TAG_TO_CREATE)).thenReturn(TAG_TO_CREATE)
        val actual = tagService.create(TAG_TO_CREATE)
        assertEquals(TAG_TO_CREATE, actual)
    }

    @Test
    fun createShouldThrowInvalidDataException() {
        whenever(tagRepository.save(INVALID_TAG)).thenThrow(InvalidDataException())
        assertThrows<InvalidDataException> { tagService.create(INVALID_TAG) }
    }

    @Test
    fun createShouldThrowDuplicateEntityException() {
        whenever(tagRepository.findByName(FIRST_TEST_TAG.name)).thenReturn(Optional.of(FIRST_TEST_TAG))
        assertThrows<DuplicateEntityException> { tagService.create(FIRST_TEST_TAG) }
    }

    @Test
    fun getById() {
        whenever(tagRepository.findById(TEST_ID)).thenReturn(Optional.of(FIRST_TEST_TAG))
        val actual = tagService.getById(TEST_ID)
        assertEquals(FIRST_TEST_TAG, actual)
    }

    @Test
    fun getByIdShouldThrowEntityNotFoundException() {
        whenever(tagRepository.findById(NOT_EXIST_ID)).thenThrow(EntityNotFoundException())
        assertThrows<EntityNotFoundException> { tagService.getById(NOT_EXIST_ID) }
    }

    @Test
    fun deleteByIdShouldThrowEntityNotFoundException() {
        assertThrows<EntityNotFoundException> { tagService.deleteById(NOT_EXIST_ID) }
    }

    @Test
    fun getMostWidelyUsedTagOfUserWithHighestCostOfAllOrders() {
        whenever(userRepository.findById(TEST_ID)).thenReturn(Optional.of(FIRST_TEST_USER))
        whenever(tagRepository.getMostWidelyUsedTagOfUserWithHighestCostOfAllOrders(TEST_ID)).thenReturn(
            Optional.of(
                FIRST_TEST_TAG
            )
        )
        val actual = tagService.getMostWidelyUsedTagOfUserWithHighestCostOfAllOrders(TEST_ID)
        assertEquals(FIRST_TEST_TAG, actual)
    }
}
