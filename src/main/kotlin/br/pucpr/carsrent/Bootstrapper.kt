package br.pucpr.carsrent

import br.pucpr.carsrent.roles.Role
import br.pucpr.carsrent.roles.RoleRepository
import br.pucpr.carsrent.users.User
import br.pucpr.carsrent.users.UserRepository
import org.springframework.context.ApplicationListener
import org.springframework.context.event.ContextRefreshedEvent
import org.springframework.stereotype.Component

@Component
class Bootstrapper(
    val userRepository: UserRepository,
    val roleRepository: RoleRepository
): ApplicationListener<ContextRefreshedEvent> {
    override fun onApplicationEvent(event: ContextRefreshedEvent) {
        val adminRole = roleRepository.findByName("ADMIN") ?: roleRepository
            .save(Role(name = "ADMIN", description = "System Administrator"))
            .also { roleRepository.save(Role(name = "USER", description = "Premium User")) }

        if (userRepository.findByRole(adminRole.name).isEmpty()) {
            val admin = User(
                name = "Auth Server Administrator",
                email = "admin@authserver.com",
                password = "admin"
            )

            admin.roles.add(adminRole)
            userRepository.save(admin)
        }
    }
}