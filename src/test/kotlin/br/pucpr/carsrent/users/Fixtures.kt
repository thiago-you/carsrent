package br.pucpr.carsrent.users

import br.pucpr.carsrent.roles.Role

fun _user(id: Long? = null, name: String = "name", roles: List<String> = emptyList()) = User(
    id = id,
    name = name,
    email = "$name@email.com",
    password = "pass123",
    roles = roles.mapIndexed { i, v -> Role(id = i.toLong(), name = v) }.toMutableSet()
)