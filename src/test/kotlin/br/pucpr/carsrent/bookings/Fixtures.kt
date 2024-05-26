package br.pucpr.carsrent.bookings

import br.pucpr.carsrent.users.User
import br.pucpr.carsrent.users._user
import br.pucpr.carsrent.vehicles.Vehicle
import br.pucpr.carsrent.vehicles._vehicle

fun _booking(
    id: Long? = 1,
    user: User? = null,
    vehicle: Vehicle? = null,
    status: BookingStatus = BookingStatus.OPEN
) = Booking(
    id = id,
    status = status.toString(),
    user = user ?: _user(id = 1),
    vehicle = vehicle ?: _vehicle(id = 1),
)