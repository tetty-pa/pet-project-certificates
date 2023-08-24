package com.epam.esm.model.entity.audit

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import javax.persistence.PrePersist
import javax.persistence.PreRemove
import javax.persistence.PreUpdate

@Component
class EntityAuditListener {
    companion object {
        private val LOGGER: Logger = LoggerFactory.getLogger(EntityAuditListener::class.java)
    }

    @PrePersist
    private fun onPrePersist(entity: Any) {
        LOGGER.info("persist object: {}", entity)
    }

    @PreUpdate
    private fun onPreUpdate(entity: Any) {
        LOGGER.info("update object: {}", entity)
    }

    @PreRemove
    fun onPreRemove(entity: Any) {
        LOGGER.info("remove object: {}", entity)
    }
}
