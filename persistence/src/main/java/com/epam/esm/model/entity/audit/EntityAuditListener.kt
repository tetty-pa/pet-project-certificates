package com.epam.esm.model.entity.audit

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import javax.persistence.PrePersist
import javax.persistence.PreRemove
import javax.persistence.PreUpdate

@Component
class EntityAuditListener {
    private val LOGGER = LoggerFactory.getLogger(EntityAuditListener::class.java)
    @PrePersist
    private fun onPrePersist(`object`: Any) {
        LOGGER.info("persist object: $`object`")
    }

    @PreUpdate
    private fun onPreUpdate(`object`: Any) {
        LOGGER.info("update object: $`object`")
    }

    @PreRemove
    fun onPreRemove(`object`: Any) {
        LOGGER.info("remove object: $`object`")
    }
}
