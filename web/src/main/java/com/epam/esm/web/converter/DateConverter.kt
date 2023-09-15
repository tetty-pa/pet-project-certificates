package com.epam.esm.web.converter

import com.google.protobuf.Timestamp
import org.springframework.stereotype.Component
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset

private const val MILLISECONDS_PER_SECOND = 1000

@Component
class DateConverter {
    fun localDateTimeToTimestamp(dateTime: LocalDateTime): Timestamp {
        return Timestamp.newBuilder()
            .setSeconds(dateTime.toInstant(ZoneOffset.UTC).toEpochMilli() / MILLISECONDS_PER_SECOND)
            .setNanos(dateTime.nano)
            .build()
    }

    fun timestampToLocalDateTime(timestamp: Timestamp): LocalDateTime {
        val instant = Instant.ofEpochSecond(timestamp.seconds, timestamp.nanos.toLong())
        return instant.atZone(ZoneId.of("UTC")).toLocalDateTime()
    }
}
