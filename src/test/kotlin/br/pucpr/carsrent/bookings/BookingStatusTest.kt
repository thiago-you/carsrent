package br.pucpr.carsrent.bookings

import br.pucpr.carsrent.exceptions.BadRequestException
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContainIgnoringCase
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

class BookingStatusTest {
    @Test
    fun `findOrThrow throws BadRequestException for invalid values`() {
        val error = assertThrows<BadRequestException> {
            BookingStatus.findOrThrow("INVALID")
        }

        error.message shouldContainIgnoringCase "invalid"
    }

    @ParameterizedTest
    @ValueSource(strings = ["open", "OPEN", "oPeN"])
    fun `findOrThrow returns BookingStatus for valid OPEN values ignoring case`(status: String) {
        BookingStatus.findOrThrow(status) shouldBe BookingStatus.OPEN
    }

    @ParameterizedTest
    @ValueSource(strings = ["confirmed", "CONFIRMED", "cOnFiRmEd"])
    fun `findOrThrow returns BookingStatus for valid CONFIRMED values ignoring case`(status: String) {
        BookingStatus.findOrThrow(status) shouldBe BookingStatus.CONFIRMED
    }

    @ParameterizedTest
    @ValueSource(strings = ["canceled", "CANCELED", "cAnCeLeD"])
    fun `findOrThrow returns BookingStatus for valid CANCELED values ignoring case`(status: String) {
        BookingStatus.findOrThrow(status) shouldBe BookingStatus.CANCELED
    }
}