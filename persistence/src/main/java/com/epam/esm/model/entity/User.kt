package com.epam.esm.model.entity

import com.epam.esm.model.entity.audit.EntityAuditListener
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EntityListeners
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.OneToOne
import jakarta.persistence.Table
import jakarta.validation.constraints.Size
import org.springframework.hateoas.RepresentationModel

@Entity
@Table(name = "users")
@EntityListeners(EntityAuditListener::class)
data class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0,

    @Column(length = 80, nullable = false)
    @Size(min = 1, max = 80, message = "user.invalidName")
    var name: String = "",

    @Column(length = 80, nullable = false)
    var email: String = "",

    @Column(length = 80, nullable = false)
    var password: String = "",

    @OneToOne
    @JoinColumn(name = "role_id", nullable = false)
    var role: Role? = Role(2)
) : RepresentationModel<User>()
