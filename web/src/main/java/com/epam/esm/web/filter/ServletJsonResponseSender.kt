package com.epam.esm.web.filter

import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.http.HttpServletResponse
import org.springframework.stereotype.Component
import java.io.IOException

@Component
class ServletJsonResponseSender {
    @Throws(IOException::class)
    fun send(httpServletResponse: HttpServletResponse, responseObject: Any?) {
        httpServletResponse.characterEncoding = RESPONSE_CHAR_ENCODING
        httpServletResponse.status = HttpServletResponse.SC_UNAUTHORIZED
        httpServletResponse.contentType = RESPONSE_CONTENT_TYPE
        val json = ObjectMapper().writeValueAsString(responseObject)
        httpServletResponse.writer.write(json)
        httpServletResponse.flushBuffer()
    }

    companion object {
        private const val RESPONSE_CHAR_ENCODING = "UTF-8"
        private const val RESPONSE_CONTENT_TYPE = "application/json"
    }
}
