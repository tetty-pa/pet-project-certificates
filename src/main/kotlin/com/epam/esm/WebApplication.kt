package com.epam.esm;

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.web.reactive.config.EnableWebFlux

@EnableWebFlux
@SpringBootApplication
class WebApplication
fun main(args: Array<String>) {
    runApplication<WebApplication>(*args)
}
