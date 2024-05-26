package br.pucpr.carsrent.bookings.requests

import io.kotest.matchers.shouldBe
import io.mockk.checkUnnecessaryStub
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test

class BookingRequestTest {
    @AfterEach
    fun cleanUp() = checkUnnecessaryStub()

    @Test
    fun `toBooking should convert BookingRequest to Booking correctly`() {
        val days = 1
        val vehicleId: Long = 1
        val userId: Long = 1

        val bookingRequest = BookingRequest(days, vehicleId, userId)
        val booking = bookingRequest.toBooking()

        booking.days shouldBe days
        booking.vehicle?.id shouldBe vehicleId
        booking.user?.id shouldBe userId
    }

    @Test
    fun `toBooking should set default values for Booking correctly`() {
        val days = 1
        val vehicleId: Long = 1
        val userId: Long = 1
        val status = ""
        val totalPrice: Double = 0.0

        val bookingRequest = BookingRequest(days, vehicleId, userId)
        val booking = bookingRequest.toBooking()

        booking.days shouldBe days
        booking.vehicle?.id shouldBe vehicleId
        booking.user?.id shouldBe userId
        booking.id shouldBe null
        booking.status shouldBe status
        booking.totalPrice shouldBe totalPrice
    }
}