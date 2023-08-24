package com.epam.esm.web;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


@SpringBootApplication
@ComponentScan("com.epam.esm")
@EnableJpaRepositories("com.epam.esm.repository")
class WebApplication

fun main(args: Array<String>) {
    runApplication<WebApplication>(*args)
}
