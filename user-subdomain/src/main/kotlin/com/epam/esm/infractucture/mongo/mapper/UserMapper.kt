package com.epam.esm.infractucture.mongo.mapper

import com.epam.esm.domain.Role
import com.epam.esm.domain.User
import com.epam.esm.infractucture.mongo.entity.RoleEntity
import com.epam.esm.infractucture.mongo.entity.UserEntity
import com.epam.esm.mapper.EntityMapper
import org.springframework.stereotype.Component

@Component
class UserMapper : EntityMapper<User, UserEntity> {
    override fun mapToDomain(entity: UserEntity): User {
        return User(
            id = entity.id,
            name = entity.name,
            email = entity.email,
            password = entity.password,
            role = Role.valueOf(entity.role.name)
        )
    }

    override fun mapToEntity(domain: User): UserEntity {
        return UserEntity(
            name = domain.name,
            email = domain.email,
            password = domain.password,
            role = RoleEntity.valueOf(domain.role.name)
        )
    }
}
