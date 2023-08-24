package com.epam.esm.model.entity

import com.epam.esm.model.entity.audit.EntityAuditListener
import org.springframework.hateoas.RepresentationModel
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.EntityListeners
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.OneToOne

import javax.persistence.Table
import javax.validation.constraints.Size

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

        /*@OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
        val orders: List<Order> = ArrayList(),
    */
        @OneToOne
        @JoinColumn(name = "role_id", nullable = false)
        var role: Role? = Role(2)
) : RepresentationModel<User>()
