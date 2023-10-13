package com.epam.esm.logger


@Target( AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class Logging(
    val isRequest: Boolean = true,
    val isResponse: Boolean = true
)
