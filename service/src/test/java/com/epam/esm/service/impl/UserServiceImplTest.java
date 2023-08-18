package com.epam.esm.service.impl;


import com.epam.esm.exception.DuplicateEntityException;
import com.epam.esm.exception.EntityNotFoundException;
import com.epam.esm.exception.InvalidDataException;
import com.epam.esm.model.entity.Tag;
import com.epam.esm.model.entity.User;
import com.epam.esm.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.epam.esm.service.impl.util.Constants.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    UserRepository userRepository;

    @Mock
    PasswordEncoder passwordEncoder;

    @InjectMocks
    UserServiceImpl userService;

    @Test
    void getAll() {
        List<User> expected = Arrays.asList(FIRST_TEST_USER, SECOND_TEST_USER);
        when(userRepository.findAll(PAGE)).thenReturn(new PageImpl<>(expected));

        List<User> actual = userService.getAll(PAGE_NUM, PAGE_SIZE);

        assertEquals(expected, actual);

    }

    @Test
    void getById() {
        when(userRepository.findById(TEST_ID)).thenReturn(Optional.of(FIRST_TEST_USER));
        User actual = userService.getById(TEST_ID);
        assertEquals(FIRST_TEST_USER, actual);
    }

    @Test
    void getByIdShouldThrowEntityNotFoundException() {
        when(userRepository.findById(NOT_EXIST_ID)).thenThrow(new EntityNotFoundException());
        assertThrows(EntityNotFoundException.class, () -> userService.getById(NOT_EXIST_ID));
    }


    @Test
    void create() {
        when(userRepository.save(USER_TO_CREATE)).thenReturn(USER_TO_CREATE);
        //when(passwordEncoder.encode(USER_TO_CREATE.getPassword())).thenReturn(USER_TO_CREATE.getPassword());
        User actual = userService.create(USER_TO_CREATE);
        assertEquals(USER_TO_CREATE, actual);
    }




}
