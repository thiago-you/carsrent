package br.pucpr.carsrent.users

enum class SortDir {
    ASC, DESC;

    companion object {
        fun byName(value: String?) = entries.firstOrNull { it.name.equals(value, ignoreCase = true) } ?: ASC
    }
}