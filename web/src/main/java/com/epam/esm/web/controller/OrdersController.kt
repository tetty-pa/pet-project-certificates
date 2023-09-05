package com.epam.esm.web.controller

import com.epam.esm.model.entity.Order
import com.epam.esm.service.OrderService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/orders")
class OrdersController(private val orderService: OrderService) {
    @GetMapping("/users/{userId}")
    @ResponseStatus(HttpStatus.OK)
    fun getAllByUserId(
        @PathVariable userId: String,
        @RequestParam(value = "page", defaultValue = "0", required = false) page: Int,
        @RequestParam(value = "size", defaultValue = "25", required = false) size: Int
    ): List<Order> = orderService.getAllByUserId(userId, page, size)

    @PostMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    fun create(
        @PathVariable userId: String,
        @RequestParam certificateId: String
    ): Order = orderService.create(userId, certificateId)


    @GetMapping("/{orderId}")
    @ResponseStatus(HttpStatus.OK)
    fun getById(@PathVariable orderId: String): Order =
        orderService.getById(orderId)
}
