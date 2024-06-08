package br.pucpr.carsrent.bookings

import br.pucpr.carsrent.exceptions.BadRequestException
import br.pucpr.carsrent.security.AuthUser
import br.pucpr.carsrent.users.UserRepositorys
import br.pucpr.carsrent.vehicles.VehicleRepository
import org.slf4j.LoggerFactory
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class BookingService(
    val bookingRepository: BookingRepository,
    val vehicleRepository: VehicleRepository,
    val userRepository: UserRepositorys,
    val authUser: AuthUser
) {
    fun insert(booking: Booking): Booking {
        bookingRepository.findByVehicleId(booking.vehicle?.id!!)
            .takeIf { list -> list.any { it.status == BookingStatus.OPEN.toString() } }
            ?.also { throw BadRequestException("Vehicle already booked!") }

        val vehicle = vehicleRepository.findByIdOrNull(booking.vehicle?.id!!)
            ?: throw BadRequestException("Vehicle not found!")

        val user = userRepository.findByIdOrNull(booking.user?.id!!)
            ?: throw BadRequestException("User not found!")

        booking.status = BookingStatus.OPEN.toString()
        booking.user = user
        booking.vehicle = vehicle
        booking.totalPrice = booking.days * vehicle.price

        return bookingRepository.save(booking).also {
            log.info("Booking inserted! {}", it.id)
        }
    }

    fun updateVehicle(bookingId: Long, vehicleId: Long): Booking? {
        val booking = bookingRepository.findByIdOrNull(bookingId)
            ?: throw BadRequestException("Booking not found!")

        val vehicle = vehicleRepository.findByIdOrNull(vehicleId)
            ?: throw BadRequestException("Vehicle not found!")

        if (booking.vehicle!!.id == vehicleId) {
            throw BadRequestException("Vehicle already added to this booking!")
        }

        if (booking.status != BookingStatus.OPEN.toString()) {
            throw BadRequestException("Booking not available for update!")
        }

        booking.vehicle = vehicle
        booking.totalPrice = booking.days * vehicle.price

        return bookingRepository.save(booking).also {
            log.info("Booking updated! {}", it.id)
        }
    }

    fun updateBookingStatus(bookingId: Long, bookingStatus: BookingStatus) {
        val booking = bookingRepository.findByIdOrNull(bookingId)
            ?: throw BadRequestException("Booking not found!")

        if (booking.status != BookingStatus.OPEN.toString()) {
            throw BadRequestException("Booking not available!")
        }

        if (bookingStatus == BookingStatus.CANCELED) {
            if (booking.user?.id != authUser.getId()) {
                throw BadRequestException("User not allowed to cancel this booking!")
            }
        }

        booking.status = bookingStatus.toString()

        bookingRepository.save(booking).also {
            log.info("Booking ${bookingStatus.toString().lowercase()}! {}", it.id)
        }
    }

    fun findAll(): List<Booking> = bookingRepository.findAll()

    fun findByIdOrNull(id: Long) = bookingRepository.findByIdOrNull(id)

    fun deleteById(id: Long) {
        bookingRepository.deleteById(id)
    }

    fun findByVehicleId(vehicleId: Long) = bookingRepository.findByVehicleId(vehicleId)

    fun findByUserId(userId: Long) = bookingRepository.findByUserId(userId)

    fun findByStatus(status: BookingStatus) = bookingRepository.findByStatus(status.toString())

    companion object {
        private val log = LoggerFactory.getLogger(BookingService::class.java)
    }
}