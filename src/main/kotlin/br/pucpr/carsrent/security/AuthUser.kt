package br.pucpr.carsrent.security

import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component

@Component
class AuthUser {
    fun getId(): Long? {
        val authentication: Authentication? = SecurityContextHolder.getContext().authentication

        return when (authentication?.principal) {
            is Long -> {
                authentication.principal as Long
            }
            is UserToken -> {
                (authentication.principal as UserToken).id
            }
            else -> {
                null
            }
        }
    }
}