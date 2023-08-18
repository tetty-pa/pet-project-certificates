package com.epam.esm.web.controller

import com.epam.esm.model.entity.User
import com.epam.esm.service.UserService
import com.epam.esm.web.link.UserLinkAdder
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/users")
class UsersController(
    private val userService: UserService,
    private val userLinkAdder: UserLinkAdder
) {
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    fun getAll(
        @RequestParam(value = "page", defaultValue = "0", required = false) page: Int,
        @RequestParam(value = "size", defaultValue = "25", required = false) size: Int
    ): List<User> {

        val users = userService.getAll(page, size)
        users.forEach(userLinkAdder::addLinks)
        return users
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    fun getById(@PathVariable id: Long): User {
        val user = userService.getById(id)
        userLinkAdder.addLinks(user)
        return user
    }

}