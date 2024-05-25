package br.pucpr.carsrent.bookings

import br.pucpr.carsrent.users.UserRepository
import br.pucpr.carsrent.vehicles.VehicleRepository
import org.apache.coyote.BadRequestException
import org.slf4j.LoggerFactory
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class BookingService(
    val bookingRepository: BookingRepository,
    val vehicleRepository: VehicleRepository,
    val userRepository: UserRepository
) {
    fun insert(booking: Booking): Booking {
        if (bookingRepository.findByVehicleId(booking.vehicle?.id!!).any { it.status == "OPEN" }) {
            throw BadRequestException("Vehicle already booked!")
        }

        val vehicle = vehicleRepository.findByIdOrNull(booking.vehicle?.id!!)
            ?: throw BadRequestException("Vehicle not found!")

        val user = userRepository.findByIdOrNull(booking.user?.id!!)
            ?: throw BadRequestException("User not found!")

        booking.status = "OPEN"
        booking.user = user
        booking.vehicle = vehicle
        booking.totalPrice = booking.days * vehicle.price

        return bookingRepository.save(booking).also {
            log.info("Booking inserted! {}", it.id)
        }
    }

    fun findAll(): MutableList<Booking> = bookingRepository.findAll()

    fun findByIdOrNull(id: Long) = bookingRepository.findByIdOrNull(id)

    fun deleteById(id: Long) {
        bookingRepository.deleteById(id)
    }

    fun findByVehicleId(vehicleId: Long) = bookingRepository.findByVehicleId(vehicleId)

    fun findByUserId(userId: Long) = bookingRepository.findByUserId(userId)

    fun findByStatus(status: String) = bookingRepository.findByStatus(status)

    companion object {
        private val log = LoggerFactory.getLogger(BookingService::class.java)
    }
}