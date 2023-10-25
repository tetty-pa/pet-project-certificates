package com.epam.esm.redis

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory
import org.springframework.data.redis.core.ReactiveRedisTemplate
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer
import org.springframework.data.redis.serializer.RedisSerializationContext
import org.springframework.data.redis.serializer.StringRedisSerializer


open class RedisConfig {
    fun <T : Any> reactiveRedisTemplate(
        connectionFactory: ReactiveRedisConnectionFactory,
        type: Class<T>
    ): ReactiveRedisTemplate<String, T> {
        val objectMapper = ObjectMapper().findAndRegisterModules()

        val valueSerializer =
            Jackson2JsonRedisSerializer(objectMapper, type)

        val serializationContext =
            RedisSerializationContext.newSerializationContext<String, T>(StringRedisSerializer())
                .value(valueSerializer)
                .build()
        return ReactiveRedisTemplate(connectionFactory, serializationContext)
    }
}
