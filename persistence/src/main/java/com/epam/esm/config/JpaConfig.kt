package com.epam.esm.config

import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.config.EnableJpaAuditing

@Configuration
@EntityScan(basePackages = ["com.epam.esm"])
@EnableJpaAuditing
class JpaConfig
