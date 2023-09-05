package com.epam.esm.config.customLogger


@Target( AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class Logging(
    val isRequest: Boolean = true,
    val isResponse: Boolean = true
)
