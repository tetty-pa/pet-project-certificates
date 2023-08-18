package com.epam.esm.service.security

import com.epam.esm.model.entity.Role
import com.epam.esm.model.entity.User
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

class PersonUserDetails : UserDetails {
    private var name: String? = null
    private var password: String? = null
    private var authorities: List<GrantedAuthority>? = null

    constructor()
    constructor(user: User) {
        name = user.name
        password = user.password
        authorities = listOf<GrantedAuthority>(
                SimpleGrantedAuthority("ROLE_" +
                        Role.getRole((user.role?.id ?: 1) - 1)
                                .name
                                .uppercase()))
    }

    override fun getAuthorities(): Collection<GrantedAuthority> {
        return authorities!!
    }

    override fun getPassword(): String {
        return password!!
    }

    override fun getUsername(): String {
        return name!!
    }

    override fun isAccountNonExpired(): Boolean {
        return true
    }

    override fun isAccountNonLocked(): Boolean {
        return true
    }

    override fun isCredentialsNonExpired(): Boolean {
        return true
    }

    override fun isEnabled(): Boolean {
        return true
    }
}
