package com.epam.esm.infrastucture.database.redis.config

import com.epam.esm.infrastucture.database.entity.TagEntity
import com.epam.esm.redis.RedisConfig
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory
import org.springframework.data.redis.core.ReactiveRedisTemplate

@Configuration
class TagRedisConfig(
    private val connectionFactory: ReactiveRedisConnectionFactory,
) : RedisConfig() {
    @Bean
    fun tagReactiveRedisTemplate(): ReactiveRedisTemplate<String, TagEntity> {
        return reactiveRedisTemplate(connectionFactory, TagEntity::class.java)
    }
}
