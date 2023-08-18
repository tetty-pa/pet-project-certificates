package com.epam.esm.repository

import com.epam.esm.model.entity.GiftCertificate
import com.epam.esm.model.entity.Tag
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*


interface GiftCertificateRepository : JpaRepository<GiftCertificate, Long> {
    /**
     * Gets all Gift Certificates with parameters.
     *
     * @param name Gift Certificate`s name to find
     * @param description Gift Certificate`s description to find
     * @param pageable object with pagination info(page number, page size)
     * @param tagList Gift Certificate`s list of tags to find
     * @return List of Gift Certificates
     */

    fun getGiftCertificatesByNameLikeAndDescriptionLikeAndTagListIn(
        name: String,
        description: String,
        tagList: List<Tag>,
        pageable: Pageable
    ): List<GiftCertificate>


    /**
     * Gets Gift Certificate by  name.
     *
     * @param name Gift Certificate  name to get
     * @return Optional<GiftCertificate> Certificate if founded or Empty if not
     */
    fun findByName(name: String): Optional<GiftCertificate>

}