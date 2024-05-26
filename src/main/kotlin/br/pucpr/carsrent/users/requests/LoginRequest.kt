package br.pucpr.carsrent.users.requests

import io.swagger.v3.oas.annotations.media.Schema

data class LoginRequest(
    @Schema(example = "admin@admin.com")
    var email: String? = null,
    @Schema(example = "admin")
    var password: String? = null
)
