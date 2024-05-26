package br.pucpr.carsrent.bookings

import br.pucpr.carsrent.exceptions.BadRequestException

enum class BookingStatus {
    OPEN, CONFIRMED, CANCELED;

    companion object {
        fun findOrThrow(value: String?) = BookingStatus.entries
            .firstOrNull { it.name.equals(value, ignoreCase = true) }
            ?: throw BadRequestException("Invalid booking status!")
    }
}