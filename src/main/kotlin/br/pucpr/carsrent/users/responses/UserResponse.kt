package br.pucpr.carsrent.users.responses

import br.pucpr.carsrent.users.Users

data class UserResponse(
    val id: Long,
    val name: String,
    val email: String
) {
    constructor(user: Users) : this(
        id = user.id!!,
        name = user.name,
        email = user.email
    )
}
