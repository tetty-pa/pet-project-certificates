package com.epam.esm.web.exception

import com.epam.esm.exception.DuplicateEntityException
import com.epam.esm.exception.EntityNotFoundException
import com.epam.esm.exception.InvalidDataException
import com.epam.esm.exception.InvalidJwtException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import java.util.Locale

@RestControllerAdvice
class RestResponseEntityExceptionHandler(private val messageSource: MessageConfig) {
    private fun buildErrorResponse(
            message: String?, code: Int,
            status: HttpStatus
    ): ResponseEntity<ExceptionResponse> {
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
            e: EntityNotFoundException, locale: Locale
    ): ResponseEntity<ExceptionResponse> =
            buildErrorResponse(
                    resolveResourceBundle(e.message, locale),
                    CODE_ENTITY_NOT_FOUND, HttpStatus.NOT_FOUND
            )

    @ExceptionHandler(InvalidDataException::class)
    fun handleInvalidDataException(
            e: InvalidDataException, locale: Locale
    ): ResponseEntity<ExceptionResponse> =
            buildErrorResponse(
                    resolveResourceBundle(e.message, locale),
                    CODE_INVALID_DATA, HttpStatus.BAD_REQUEST
            )

    @ExceptionHandler(DuplicateEntityException::class)
    fun handleDuplicateEntityException(
            e: DuplicateEntityException, locale: Locale
    ): ResponseEntity<ExceptionResponse> =
            buildErrorResponse(
                    resolveResourceBundle(e.message, locale),
                    CODE_DUPLICATE_ENTITY, HttpStatus.NOT_FOUND
            )


    @ExceptionHandler(Exception::class)
    fun handleOtherExceptions(e: Exception): ResponseEntity<ExceptionResponse> =
            buildErrorResponse(e.message, CODE_OTHER, HttpStatus.INTERNAL_SERVER_ERROR)

    @ExceptionHandler(InvalidJwtException::class)
    fun handleInvalidJwtException(locale: Locale): ResponseEntity<ExceptionResponse> =
            buildErrorResponse(
                    resolveResourceBundle("jwt.invalid", locale),
                    CODE_INVALID_JWT, HttpStatus.UNAUTHORIZED
            )

    fun buildNoJwtResponseObject(locale: Locale): ExceptionResponse =
            ExceptionResponse(resolveResourceBundle("jwt.not.exist", locale), CODE_JWT_NOT_EXISTS)

    companion object {
        private val AVAILABLE_LOCALES: List<String> = mutableListOf("en_US", "ua_UA")
        private val DEFAULT_LOCALE = Locale("en", "US")
        private const val CODE_ENTITY_NOT_FOUND = 40002
        private const val CODE_INVALID_DATA = 42201
        private const val CODE_DUPLICATE_ENTITY = 40901
        private const val CODE_INVALID_JWT = 40100
        private const val CODE_JWT_NOT_EXISTS = 40100
        private const val CODE_OTHER = 50000
    }
}
