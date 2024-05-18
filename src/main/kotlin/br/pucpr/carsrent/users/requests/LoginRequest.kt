package br.pucpr.carsrent.users.requests

data class LoginRequest(
    var email: String? = null,
    var password: String? = null
)
