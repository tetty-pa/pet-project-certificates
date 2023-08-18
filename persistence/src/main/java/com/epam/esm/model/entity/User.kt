package com.epam.esm.model.entity

import com.epam.esm.model.entity.audit.EntityAuditListener
import org.springframework.hateoas.RepresentationModel
import javax.persistence.*
import javax.validation.constraints.Size

@Entity
@Table(name = "users")
@EntityListeners(EntityAuditListener::class)
class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0,

    var name: @Size(min = 1, max = 80, message = "user.invalidName") String = "",

    @Column(length = 80, nullable = false)
    var email: String = "",

    @Column(length = 80, nullable = false)
    var password: String = "",

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    val orders: List<Order> = ArrayList(),

    @OneToOne
    @JoinColumn(name = "role_id", nullable = false)
    var role: Role? = null
) : RepresentationModel<User>() {


}