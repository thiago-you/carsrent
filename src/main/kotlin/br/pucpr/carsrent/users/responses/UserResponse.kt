package br.pucpr.carsrent.users.responses

import br.pucpr.carsrent.users.User

data class UserResponse(
    val id: Long,
    val name: String,
    val email: String
) {
    constructor(user: User) : this(
        id = user.id!!,
        name = user.name,
        email = user.email
    )
}
