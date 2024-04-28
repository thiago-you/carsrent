package br.pucpr.carsrent.roles

data class RoleResponse(
    val name: String,
    val description: String
) {
    constructor(role: Role) : this(role.name, role.description)
}