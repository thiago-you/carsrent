package br.pucpr.carsrent.bookings

import org.springframework.data.jpa.repository.JpaRepository

interface BookingRepository : JpaRepository<Booking, Long> {
    fun findByVehicleId(vehicleId: Long): List<Booking>
    fun findByUserId(userId: Long): List<Booking>
    fun findByStatus(status: String): List<Booking>
    fun findByUserIdAndVehicleId(userId: Long, vehicleId: Long): Booking?
}