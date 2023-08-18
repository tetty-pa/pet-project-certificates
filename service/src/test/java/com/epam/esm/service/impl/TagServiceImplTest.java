package com.epam.esm.service.impl;


import com.epam.esm.exception.DuplicateEntityException;
import com.epam.esm.exception.EntityNotFoundException;
import com.epam.esm.exception.InvalidDataException;
import com.epam.esm.model.entity.Tag;
import com.epam.esm.repository.TagRepository;
import com.epam.esm.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.epam.esm.service.impl.util.Constants.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TagServiceImplTest {


    @Mock
    TagRepository tagRepository;

    @Mock
    UserRepository userRepository;

    @InjectMocks
    TagServiceImpl tagService;

    @Test
    void getAll() {
        List<Tag> tags = Arrays.asList(FIRST_TEST_TAG, SECOND_TEST_TAG, THIRD_TEST_TAG);
        when(tagRepository.findAll(PAGE)).thenReturn(new PageImpl<>(tags));
        List<Tag> actual = tagService.getAll(PAGE_NUM, PAGE_SIZE);

        assertEquals(tags, actual);
    }

    @Test
    void create() {
        when(tagRepository.save(TAG_TO_CREATE)).thenReturn(TAG_TO_CREATE);
        Tag actual = tagService.create(TAG_TO_CREATE);
        assertEquals(TAG_TO_CREATE, actual);
    }

    @Test
    void createShouldThrowInvalidDataException() {
        when(tagRepository.save(INVALID_TAG)).thenThrow(new InvalidDataException());
        assertThrows(InvalidDataException.class, () -> tagService.create(INVALID_TAG));
    }

    @Test
    void createShouldThrowDuplicateEntityException() {
        // when(tagRepository.findByName( FIRST_TEST_TAG.getName())).thenReturn(Optional.of(FIRST_TEST_TAG));
        assertThrows(DuplicateEntityException.class, () -> tagService.create(FIRST_TEST_TAG));
    }

    @Test
    void getById() {
        when(tagRepository.findById(TEST_ID)).thenReturn(Optional.of(FIRST_TEST_TAG));
        Tag actual = tagService.getById(TEST_ID);
        assertEquals(FIRST_TEST_TAG, actual);
    }

    @Test
    void getByIdShouldThrowEntityNotFoundException() {
        when(tagRepository.findById(NOT_EXIST_ID)).thenThrow(new EntityNotFoundException());
        assertThrows(EntityNotFoundException.class, () -> tagService.getById(NOT_EXIST_ID));
    }



    @Test
    void deleteByIdShouldThrowEntityNotFoundException() {
        assertThrows(EntityNotFoundException.class, () -> tagService.deleteById(NOT_EXIST_ID));
    }

    @Test
    void getMostWidelyUsedTagOfUserWithHighestCostOfAllOrders() {
        when(userRepository.findById(TEST_ID)).thenReturn(Optional.of(FIRST_TEST_USER));
        when(tagRepository.getMostWidelyUsedTagOfUserWithHighestCostOfAllOrders(TEST_ID)).thenReturn(Optional.of(FIRST_TEST_TAG));
        Tag actual = tagService.getMostWidelyUsedTagOfUserWithHighestCostOfAllOrders(TEST_ID);
        assertEquals(FIRST_TEST_TAG, actual);
    }


}
