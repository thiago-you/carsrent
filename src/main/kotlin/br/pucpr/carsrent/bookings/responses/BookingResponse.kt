package br.pucpr.carsrent.bookings.responses

import br.pucpr.carsrent.bookings.Booking
import br.pucpr.carsrent.users.responses.UserResponse
import br.pucpr.carsrent.vehicles.responses.VehicleResponse

data class BookingResponse(
    val id: Long,
    val status: String,
    val days: Int,
    val totalPrice: Double,
    val vehicle: VehicleResponse,
    val user: UserResponse,
) {
    constructor(booking: Booking) : this(
        id = booking.id!!,
        status = booking.status,
        days = booking.days,
        totalPrice = booking.totalPrice,
        vehicle = VehicleResponse(booking.vehicle!!),
        user = UserResponse(booking.user!!)
    )
}
