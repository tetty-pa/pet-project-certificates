package com.epam.esm.model.entity

import com.epam.esm.model.entity.audit.EntityAuditListener
import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.hateoas.RepresentationModel
import java.math.BigDecimal
import java.time.ZonedDateTime
import javax.persistence.*
import javax.validation.constraints.Min
import javax.validation.constraints.Size

@Entity
@Table(name = "gift_certificates")
@EntityListeners(
    EntityAuditListener::class
)
data class GiftCertificate(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = 0,

    @Column(length = 80, nullable = false)
    var name: @Size(min = 1, max = 80, message = "gift-certificate.invalidName") String? = "",

    @Column(length = 250, nullable = false)
    var description: @Size(min = 1, max = 250, message = "gift-certificate.invalidDescription") String? = "",

    @Column(nullable = false)
    var price: @Min(value = 1, message = "gift-certificate.invalidPrice") BigDecimal? = BigDecimal.ZERO,

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSX")
    @Column(name = "create_date", nullable = false, updatable = false)
    var createDate: ZonedDateTime? = null,

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSX")
    @Column(name = "last_updated_date", nullable = false)
    var lastUpdatedDate: ZonedDateTime? = null,

    @Column(nullable = false)
    var duration: @Min(value = 1, message = "gift-certificate.invalidDuration") Int? = 0,


    @JoinTable(
        name = "gift_certificate_has_tag",
        joinColumns = [JoinColumn(name = "gift_certificate_id", referencedColumnName = "ID")],
        inverseJoinColumns = [JoinColumn(name = "tag_id", referencedColumnName = "ID")]
    )
    @ManyToMany(cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    var tagList: MutableList<Tag>? = mutableListOf()

) : RepresentationModel<GiftCertificate>(){
}
