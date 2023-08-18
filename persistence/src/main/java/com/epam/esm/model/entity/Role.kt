package com.epam.esm.model.entity

import com.epam.esm.model.entity.audit.EntityAuditListener
import org.springframework.hateoas.RepresentationModel
import javax.persistence.*

@Entity
@Table(name = "roles")
@EntityListeners(
    EntityAuditListener::class
)
class Role(
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
