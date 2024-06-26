package br.pucpr.carsrent.users

import br.pucpr.carsrent.bookings.BookingRepository
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
class UserServices(
    val userRepository: UserRepositorys,
    val roleRepository: RoleRepository,
    val bookingRepository: BookingRepository,
    val jwt: Jwt
) {
    fun insert(user: Users): Users {
        if (userRepository.findByEmail(user.email) != null) {
            throw BadRequestException("User with e-mail ${user.email} already exists!")
        }

        return userRepository.save(user).also {
            log.info("User inserted! {}", it.id)
        }
    }

    fun findAll(sortDir: SortDirectory, role: String?) = role?.let {
        when (sortDir) {
            SortDirectory.ASC -> userRepository.findByRole(role.uppercase()).sortedBy { it.name }
            SortDirectory.DESC -> userRepository.findByRole(role.uppercase()).sortedByDescending { it.name }
        }
    } ?: run {
        when (sortDir) {
            SortDirectory.ASC -> userRepository.findAll(Sort.by("name").ascending())
            SortDirectory.DESC -> userRepository.findAll(Sort.by("name").descending())
        }
    }

    fun findByIdOrNull(id: Long) = userRepository.findByIdOrNull(id)

    fun deleteById(id: Long) {
        userRepository.findByRole("ADMIN")
            .takeIf { it.size == 1 }
            ?.firstOrNull { it.id == id }?.let {
                throw BadRequestException("Cannot delete the last admin user!")
            }

        bookingRepository.findByUserId(id)
            .takeIf { it.isNotEmpty() }
            ?.let { throw BadRequestException("User has bookings!") }

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
        private val log = LoggerFactory.getLogger(UserServices::class.java)
    }
}