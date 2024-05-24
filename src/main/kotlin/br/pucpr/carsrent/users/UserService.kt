package br.pucpr.carsrent.users

import br.pucpr.carsrent.exceptions.BadRequestException
import br.pucpr.carsrent.roles.RoleRepository
import br.pucpr.carsrent.security.Jwt
import br.pucpr.carsrent.users.responses.LoginResponse
import br.pucpr.carsrent.users.responses.UserResponse
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Sort
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class UserService(
    val userRepository: UserRepository,
    val roleRepository: RoleRepository,
    val jwt: Jwt
) {
    fun insert(user: User): User {
        if (userRepository.findByEmail(user.email) != null) {
            throw BadRequestException("User with e-mail ${user.email} already exists!")
        }

        return userRepository.save(user).also {
            log.info("User inserted! {}", it.id)
        }
    }

    fun findAll(sortDir: SortDir, role: String?) = role?.let {
        when (sortDir) {
            SortDir.ASC -> userRepository.findByRole(role.uppercase()).sortedBy { it.name }
            SortDir.DESC -> userRepository.findByRole(role.uppercase()).sortedByDescending { it.name }
        }
    } ?: run {
        when (sortDir) {
            SortDir.ASC -> userRepository.findAll(Sort.by("name").ascending())
            SortDir.DESC -> userRepository.findAll(Sort.by("name").descending())
        }
    }

    fun findByIdOrNull(id: Long) = userRepository.findByIdOrNull(id)

    fun deleteById(id: Long) {
        userRepository.findByRole("ADMIN")
            .takeIf { it.size == 1 }
            ?.firstOrNull { it.id == id }?.let {
                throw BadRequestException("Cannot delete the last admin user!")
            }

        log.warn("User deleted! id={}", id)

        userRepository.deleteById(id)
    }

    fun addRole(id: Long, roleName: String): Boolean {
        val user = userRepository.findByIdOrNull(id) ?: throw IllegalArgumentException("User $id not found!")

        if (user.roles.any { it.name.equals(roleName, ignoreCase = true) }) return false

        val role = roleRepository.findByName(roleName) ?: throw IllegalArgumentException("Invalid role $roleName!")

        user.roles.add(role)
        userRepository.save(user)

        return true
    }

    fun login(email: String, password: String): LoginResponse? {
        val user = userRepository.findByEmail(email)

        if (user == null) {
            log.warn("User {} not found!", email)
            return null
        }
        if (user.password != password) {
            log.warn("Invalid password!")
            return null
        }

        log.info("User logged in! id={} name={}", user.id, user.email)

        return LoginResponse(token = jwt.createToken(user), user = UserResponse(user))
    }

    companion object {
        private val log = LoggerFactory.getLogger(UserService::class.java)
    }
}