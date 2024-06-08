package br.pucpr.carsrent.bookings

import br.pucpr.carsrent.users.Users
import br.pucpr.carsrent.users._user
import br.pucpr.carsrent.vehicles.Vehicle
import br.pucpr.carsrent.vehicles._vehicle

fun _booking(
    id: Long? = 1,
    days: Int = 1,
    user: Users? = null,
    vehicle: Vehicle? = null,
    totalPrice: Double = 0.0,
    status: BookingStatus = BookingStatus.OPEN
) = Booking(
    id = id,
    days = days,
    status = status.toString(),
    totalPrice = totalPrice,
    user = user ?: _user(id = 1),
    vehicle = vehicle ?: _vehicle(id = 1),
)