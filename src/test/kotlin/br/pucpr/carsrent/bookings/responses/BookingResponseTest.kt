package br.pucpr.carsrent.bookings.responses

import br.pucpr.carsrent.bookings.Booking
import br.pucpr.carsrent.bookings.BookingStatus
import br.pucpr.carsrent.users._user
import br.pucpr.carsrent.users.responses.UserResponse
import br.pucpr.carsrent.vehicles._vehicle
import br.pucpr.carsrent.vehicles.responses.VehicleResponse
import io.kotest.matchers.shouldBe
import io.mockk.checkUnnecessaryStub
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test

class BookingResponseTest {
    @AfterEach
    fun cleanUp() = checkUnnecessaryStub()

    @Test
    fun `constructor should create BookingResponse with same properties from Booking`() {
        val vehicle = _vehicle(id = 1)
        val user = _user(id = 1)

        val vehicleResponse = VehicleResponse(vehicle)
        val userResponse = UserResponse(user)

        val booking = Booking(
            id = 1,
            status = BookingStatus.OPEN.toString(),
            days = 2,
            totalPrice = 600.0,
            vehicle = vehicle,
            user = user
        )

        val bookingResponse = BookingResponse(booking)

        bookingResponse.id shouldBe bookingResponse.id
        bookingResponse.status shouldBe bookingResponse.status
        bookingResponse.days shouldBe bookingResponse.days
        bookingResponse.totalPrice shouldBe bookingResponse.totalPrice
        bookingResponse.vehicle shouldBe vehicleResponse
        bookingResponse.user shouldBe userResponse
    }
}