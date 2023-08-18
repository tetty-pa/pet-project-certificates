package com.epam.esm.web.controller

import com.epam.esm.model.entity.Order
import com.epam.esm.service.OrderService
import com.epam.esm.web.link.OrdersLinkAdder
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/orders")
class OrdersController(
    private val orderService: OrderService,
    private val ordersLinkAdder: OrdersLinkAdder
) {


    @GetMapping("/users/{userId}")
    @ResponseStatus(HttpStatus.OK)
    fun getAll(
        @PathVariable userId: Long,
        @RequestParam(value = "page", defaultValue = "0", required = false) page: Int,
        @RequestParam(value = "size", defaultValue = "25", required = false) size: Int
    ): List<Order> {

        val all = orderService.getAllByUserId(userId, page, size)
        all.forEach(ordersLinkAdder::addLinks)
        return all
    }

    @PostMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    fun create(
        @PathVariable userId: Long,
        @RequestParam certificateId: Long
    ): Order {

        val order = orderService.create(userId, certificateId)
        ordersLinkAdder.addLinks(order)
        return order
    }

    @GetMapping("/{orderId}")
    @ResponseStatus(HttpStatus.OK)
    fun getById(@PathVariable orderId: Long): Order {
        val order = orderService.getById(orderId)
        ordersLinkAdder.addLinks(order)
        return order
    }
}