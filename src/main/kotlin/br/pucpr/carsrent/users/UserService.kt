package br.pucpr.carsrent.users

import br.pucpr.carsrent.roles.RoleRepository
import org.springframework.data.domain.Sort
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class UserService(
    val userRepository: UserRepository,
    val roleRepository: RoleRepository
) {
    fun save(user: User) = userRepository.save(user)

    fun findAll(sortDir: SortDir): MutableList<User> = when (sortDir) {
        SortDir.ASC -> userRepository.findAll(Sort.by("name").ascending())
        SortDir.DESC -> userRepository.findAll(Sort.by("name").descending())
    }

    fun findByIdOrNull(id: Long) = userRepository.findByIdOrNull(id)

    fun deleteById(id: Long) = userRepository.deleteById(id)

    fun addRole(id: Long, roleName: String): Boolean {
        val user = userRepository.findByIdOrNull(id) ?: throw IllegalArgumentException("User $id not found!")

        if (user.roles.any { it.name.equals(roleName, ignoreCase = true) }) return false

        val role = roleRepository.findByName(roleName) ?: throw IllegalArgumentException("Invalid role $roleName!")

        user.roles.add(role)
        userRepository.save(user)

        return true
    }
}