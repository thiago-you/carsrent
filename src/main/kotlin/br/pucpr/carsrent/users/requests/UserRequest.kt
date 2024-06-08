package br.pucpr.carsrent.users.requests

import br.pucpr.carsrent.users.Users
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank

data class UserRequest(
    @field:Email
    val email: String?,
    @field:NotBlank
    val password: String?,
    val name: String?
) {
    fun toUser() = Users(
        email = email!!,
        password = password!!,
        name = name ?: ""
    )
}
