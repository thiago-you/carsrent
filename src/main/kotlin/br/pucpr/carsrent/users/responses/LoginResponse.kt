package br.pucpr.carsrent.users.responses

data class LoginResponse(
    var token: String? = null,
    var user: UserResponse? = null
)
