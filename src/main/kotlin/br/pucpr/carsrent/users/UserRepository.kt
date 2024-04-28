package br.pucpr.carsrent.users

import org.springframework.stereotype.Component

@Component
class UserRepository {
    private var lastId = 0L
    private val users = mutableMapOf<Long, User>()

    fun save(user: User): User {
        val id = user.id

        if (id == null || id == 0L) {
            user.id = ++lastId
            users[lastId] = user
        } else {
            users[id] = user
        }

        return user
    }

    fun findAll(sortDir: SortDir) = when (sortDir) {
        SortDir.ASC -> users.values.sortedBy { it.name }
        SortDir.DESC -> users.values.sortedByDescending { it.name }
    }

    fun findByIdOrNull(id: Long) = users[id]

    fun deleteById(id: Long) = users.remove(id)
}