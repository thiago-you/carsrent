package br.pucpr.carsrent.security

import br.pucpr.carsrent.users.Users
import com.fasterxml.jackson.annotation.JsonIgnore

data class UserToken(
    val id: Long,
    val name: String,
    val roles: Set<String>
) {
    constructor() : this(0, "", emptySet())

    constructor(user: Users) : this(
        id = user.id!!,
        name = user.name,
        roles = user.roles.map { it.name }.toSortedSet()
    )

    @get:JsonIgnore
    val isAdmin: Boolean get() = "ADMIN" in roles
}
