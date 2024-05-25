package br.pucpr.carsrent.roles.responses

import br.pucpr.carsrent.roles.Role

data class RoleResponse(
    val name: String,
    val description: String
) {
    constructor(role: Role) : this(role.name, role.description)
}