package com.epam.esm.config.customLogger


@Target( AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class Logging(
    val isRequest: Boolean = true,
    val isResponse: Boolean = true
)
