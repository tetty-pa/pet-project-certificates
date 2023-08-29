package com.epam.esm.model.entity

import com.epam.esm.model.entity.audit.EntityAuditListener
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EntityListeners
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.springframework.hateoas.RepresentationModel

@Entity
@Table(name = "roles")
@EntityListeners(EntityAuditListener::class)
data class Role(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id: Long = 0,

        @Column(name = "name", length = 60, unique = true)
        var name: String = ""
) : RepresentationModel<Role>() {
    enum class RoleType {
        GUEST,
        USER,
        ADMIN
    }

    companion object {
        fun getRole(roleId: Long): RoleType {
            return RoleType.entries[roleId.toInt()]
        }
    }
}
