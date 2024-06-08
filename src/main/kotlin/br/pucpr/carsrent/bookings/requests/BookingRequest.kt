package br.pucpr.carsrent.bookings.requests

import br.pucpr.carsrent.bookings.Booking
import br.pucpr.carsrent.users.Users
import br.pucpr.carsrent.vehicles.Vehicle

data class BookingRequest(
    val days: Int,
    val vehicleId: Long,
    val userId: Long,
) {
    fun toBooking() = Booking(
        days = days,
        vehicle = Vehicle(id = vehicleId),
        user = Users(id = userId)
    )
}
