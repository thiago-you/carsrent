package br.pucpr.carsrent.users

import br.pucpr.carsrent.exceptions.BadRequestException

enum class SortDir {
    ASC, DESC;

    companion object {
        fun findOrThrow(value: String?) = entries
            .firstOrNull { it.name.equals(value, ignoreCase = true) }
            ?: throw BadRequestException("Invalid sort dir!")
    }
}