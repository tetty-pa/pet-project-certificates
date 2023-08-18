package com.epam.esm.model.entity

import com.epam.esm.model.entity.audit.EntityAuditListener
import org.springframework.hateoas.RepresentationModel
import java.math.BigDecimal
import java.time.ZonedDateTime
import javax.persistence.*
import javax.validation.constraints.Size

@Entity
@Table(name = "tags")
@EntityListeners(
    EntityAuditListener::class
)
class Tag : RepresentationModel<Tag> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0

    var name: @Size(min = 1, max = 80, message = "tag.invalidName") String = ""

    /*@ManyToMany(cascade = [CascadeType.PERSIST], fetch = FetchType.LAZY)
    @JoinTable(
        name = "gift_certificate_has_tag",
        joinColumns = [JoinColumn(name = "gift_certificate_id", referencedColumnName = "ID")],
        inverseJoinColumns = [JoinColumn(name = "tag_id", referencedColumnName = "ID")]
    )
    var tagList:List<GiftCertificate1> = ArrayList()*/
    constructor()
    constructor(
        id: Long,
        name: String
    ) {
        this.id = id
        this.name = name
    }


}