package com.epam.esm.infrastucture.persistence.mapper

import com.epam.esm.domain.Order
import com.epam.esm.infrastucture.persistence.entity.OrderEntity
import com.epam.esm.mapper.EntityMapper
import org.springframework.stereotype.Component

@Component
class OrderMapper : EntityMapper<Order, OrderEntity> {
    override fun mapToDomain(entity: OrderEntity): Order {
        return Order(
            id = entity.id,
            cost = entity.cost,
            orderDate = entity.orderDate,
            userId = entity.userId,
            giftCertificateId = entity.giftCertificateId
        )
    }

    override fun mapToEntity(domain: Order): OrderEntity {
        return OrderEntity(
            cost = domain.cost,
            orderDate = domain.orderDate,
            userId = domain.userId,
            giftCertificateId = domain.giftCertificateId
        )
    }
}
