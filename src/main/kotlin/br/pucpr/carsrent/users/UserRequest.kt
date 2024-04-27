package br.pucpr.carsrent.users

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank

data class UserRequest(
    @field:Email
    val email: String?,
    @field:NotBlank
    val password: String?,
    val name: String?
) {
    fun toUser() = User(
        email = email!!,
        password = password!!,
        name = name ?: ""
    )
}
