package br.pucpr.carsrent.roles.requests

import br.pucpr.carsrent.roles.Role
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern

data class RoleRequest(
    @Pattern(regexp = "^[A-Z][A-Z0-9]*$")
    @Schema(example = "ADMIN")
    val name: String?,
    @NotBlank
    val description: String?
) {
    fun toRole() = Role(
        name = name!!.uppercase(),
        description = description!!
    )
}