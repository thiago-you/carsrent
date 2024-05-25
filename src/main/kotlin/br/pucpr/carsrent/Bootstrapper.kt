package br.pucpr.carsrent

import br.pucpr.carsrent.bookings.Booking
import br.pucpr.carsrent.bookings.BookingRepository
import br.pucpr.carsrent.roles.Role
import br.pucpr.carsrent.roles.RoleRepository
import br.pucpr.carsrent.users.User
import br.pucpr.carsrent.users.UserRepository
import br.pucpr.carsrent.vehicles.Vehicle
import br.pucpr.carsrent.vehicles.VehicleRepository
import org.springframework.context.ApplicationListener
import org.springframework.context.event.ContextRefreshedEvent
import org.springframework.stereotype.Component

@Component
class Bootstrapper(
    val userRepository: UserRepository,
    val roleRepository: RoleRepository,
    val vehicleRepository: VehicleRepository,
    val bookingRepository: BookingRepository
): ApplicationListener<ContextRefreshedEvent> {
    override fun onApplicationEvent(event: ContextRefreshedEvent) {
        val adminRole = setupAdminRole()
        val adminUser = setupAdminUser(adminRole)
        val vehicle = setupVehicle()

        setupBooking(adminUser, vehicle)
    }

    private fun setupAdminRole(): Role {
        return roleRepository.findByName("ADMIN") ?: roleRepository
            .save(Role(name = "ADMIN", description = "System Administrator"))
            .also { roleRepository.save(Role(name = "USER", description = "Premium User")) }
    }

    private fun setupAdminUser(adminRole: Role): User {
        val usersList = userRepository.findByRole(adminRole.name).toMutableList()

        if (usersList.isEmpty()) {
            val admin = User(
                name = "Auth Server Administrator",
                email = "admin@admin.com",
                password = "admin"
            )

            admin.roles.add(adminRole)

            userRepository.save(admin).also {
                usersList.add(it)
            }
        }

        return usersList.first()
    }

    private fun setupVehicle(): Vehicle {
        val vehicleList = vehicleRepository.findAll().toMutableList()

        if (vehicleList.isEmpty()) {
            val vehicle = Vehicle(
                brand = "Mitsubishi",
                model = "Lancer",
                modelYear = 2015,
                price = 571.33,
                color = "White",
                category = "Sedan"
            )

            vehicleRepository.save(vehicle).also {
                vehicleList.add(it)
            }
        }

        return vehicleList.first()
    }

    private fun setupBooking(user: User, vehicle: Vehicle) {
        if (bookingRepository.findByUserIdAndVehicleId(user.id!!, vehicle.id!!) == null) {
            val booking = Booking(
                user = user,
                vehicle = vehicle,
                status = "OPEN",
                days = 3,
                totalPrice = 3 * vehicle.price
            )

            bookingRepository.save(booking)
        }
    }
}