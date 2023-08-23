package com.epam.esm.web.exception

import com.epam.esm.exception.DuplicateEntityException
import com.epam.esm.exception.EntityNotFoundException
import com.epam.esm.exception.InvalidDataException
import com.epam.esm.exception.InvalidJwtException
import com.epam.esm.exception.InvalidParameterException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import java.util.Locale

@RestControllerAdvice
class RestResponseEntityExceptionHandler(private val messageSource: MessageConfig){
    private fun buildErrorResponse(message: String?, code: Int,
                                   status: HttpStatus): ResponseEntity<ExceptionResponse> {
        val response = ExceptionResponse(message!!, code)
        return ResponseEntity(response, status)
    }
    private fun resolveResourceBundle(key: String?, locale: Locale): String {
        var locale = locale
        if (!AVAILABLE_LOCALES.contains(locale.toString())) {
            locale = DEFAULT_LOCALE
        }
        return messageSource.messageSource().getMessage(key, null, locale)
    }
    @ExceptionHandler(EntityNotFoundException::class)
    fun handleEntityNotFoundException(
            e: EntityNotFoundException, locale: Locale): ResponseEntity<ExceptionResponse> =
         buildErrorResponse(resolveResourceBundle(e.message, locale),
                40002, HttpStatus.NOT_FOUND)

    @ExceptionHandler(InvalidDataException::class)
    fun handleInvalidDataException(
            e: InvalidDataException, locale: Locale): ResponseEntity<ExceptionResponse> =
        buildErrorResponse(resolveResourceBundle(e.message, locale),
                42201, HttpStatus.BAD_REQUEST)

    @ExceptionHandler(DuplicateEntityException::class)
    fun handleDuplicateEntityException(
            e: DuplicateEntityException, locale: Locale): ResponseEntity<ExceptionResponse> =
        buildErrorResponse(resolveResourceBundle(e.message, locale),
                40901, HttpStatus.NOT_FOUND)

    @ExceptionHandler(InvalidParameterException::class)
    fun handleDuplicateEntityException(
            e: InvalidParameterException, locale: Locale): ResponseEntity<ExceptionResponse> =
        buildErrorResponse(resolveResourceBundle(e.message, locale),
                42301, HttpStatus.BAD_REQUEST)

    @ExceptionHandler(Exception::class)
    fun handleOtherExceptions(e: Exception): ResponseEntity<ExceptionResponse> =
        buildErrorResponse(e.message, 50000, HttpStatus.INTERNAL_SERVER_ERROR)

    @ExceptionHandler(InvalidJwtException::class)
    fun handleInvalidJwtException(locale: Locale): ResponseEntity<ExceptionResponse> =
        buildErrorResponse(resolveResourceBundle("jwt.invalid", locale),
                40100, HttpStatus.UNAUTHORIZED)

    fun buildNoJwtResponseObject(locale: Locale): ExceptionResponse =
        ExceptionResponse(resolveResourceBundle("jwt.not.exist", locale), 40101)

    companion object {
        private val AVAILABLE_LOCALES: List<String> = mutableListOf("en_US", "ua_UA")
        private val DEFAULT_LOCALE = Locale("en", "US")
    }
}
