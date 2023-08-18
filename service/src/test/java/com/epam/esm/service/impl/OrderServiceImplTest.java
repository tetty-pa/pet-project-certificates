package com.epam.esm.service.impl;


import com.epam.esm.exception.EntityNotFoundException;
import com.epam.esm.model.entity.Order;
import com.epam.esm.repository.GiftCertificateRepository;
import com.epam.esm.repository.OrderRepository;
import com.epam.esm.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.epam.esm.service.impl.util.Constants.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private GiftCertificateRepository giftCertificateRepository;

    @InjectMocks
    private OrderServiceImpl orderService;


    @Test
    void getAllByUserId() {
        List<Order> expected = Arrays.asList(FIRST_TEST_ORDER, SECOND_TEST_ORDER);
        when(orderRepository.getAllByUserId(TEST_ID, PAGE)).thenReturn(expected);
        when(userRepository.findById(TEST_ID)).thenReturn(Optional.of(FIRST_TEST_USER));

        List<Order> actual = orderService.getAllByUserId(TEST_ID, PAGE_NUM, PAGE_SIZE);

        assertEquals(expected, actual);
    }

    @Test
    void getAllByUserIdShouldThrowEntityNotFoundException() {
        when(userRepository.findById(NOT_EXIST_ID)).thenThrow(new EntityNotFoundException());
        assertThrows(EntityNotFoundException.class, () -> orderService.getAllByUserId(NOT_EXIST_ID, PAGE_NUM, PAGE_SIZE));
    }

    @Test
    void create() {
        when(userRepository.findById(TEST_ID)).thenReturn(Optional.of(FIRST_TEST_USER));
        when(giftCertificateRepository.findById(3L)).thenReturn(Optional.of(THIRD_TEST_GIFT_CERTIFICATE));
        when(orderRepository.save(THIRD_TEST_ORDER)).thenReturn(THIRD_TEST_ORDER);

        Order actual = orderService.create(TEST_ID, 3);
        assertEquals(THIRD_TEST_ORDER, actual);
    }

    @Test
    void createShouldThrowEntityNotFoundException() {
        when(userRepository.findById(NOT_EXIST_ID)).thenThrow(new EntityNotFoundException());

        assertThrows(EntityNotFoundException.class, () -> orderService.create(NOT_EXIST_ID, NOT_EXIST_ID));
    }

    @Test
    void getById() {
        when(orderRepository.findById(TEST_ID)).thenReturn(Optional.of(FIRST_TEST_ORDER));
        Order actual = orderService.getById(TEST_ID);
        assertEquals(FIRST_TEST_ORDER, actual);
    }

    @Test
    void getByIdShouldThrowEntityNotFoundException() {
        when(orderRepository.findById(NOT_EXIST_ID)).thenThrow(new EntityNotFoundException());
        assertThrows(EntityNotFoundException.class, () -> orderService.getById(NOT_EXIST_ID));
    }
}
