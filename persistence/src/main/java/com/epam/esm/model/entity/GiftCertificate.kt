package com.epam.esm.model.entity

import com.epam.esm.model.entity.audit.EntityAuditListener
import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EntityListeners
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.JoinTable
import jakarta.persistence.ManyToMany
import jakarta.persistence.Table
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.Size
import org.springframework.hateoas.RepresentationModel
import java.math.BigDecimal
import java.time.ZonedDateTime

@Entity
@Table(name = "gift_certificates")
@EntityListeners(EntityAuditListener::class)
class GiftCertificate(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = 0,

    @Column(length = 80)
    @field:Size(min = 1, max = 80, message = "gift-certificate.invalidName")
    var name: String = "",

    @Column(length = 250)
    @field: Size(min = 1, max = 250, message = "gift-certificate.invalidDescription")
    var description: String = "",

    @field:Min(value = 1, message = "gift-certificate.invalidPrice")
    var price: BigDecimal = BigDecimal.ZERO,

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSX")
    @Column(name = "create_date", updatable = false)
    var createDate: ZonedDateTime? = null,

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSX")
    @Column(name = "last_updated_date")
    var lastUpdatedDate: ZonedDateTime? = null,

    @field:Min(value = 1, message = "gift-certificate.invalidDuration")
    var duration: Int = 0,

    @JoinTable(
        name = "gift_certificate_has_tag",
        joinColumns = [JoinColumn(name = "gift_certificate_id", referencedColumnName = "ID")],
        inverseJoinColumns = [JoinColumn(name = "tag_id", referencedColumnName = "ID")]
    )
    @ManyToMany(cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    var tagList: MutableList<Tag> = mutableListOf()
) : RepresentationModel<GiftCertificate>()
