package br.pucpr.carsrent.vehicles

import br.pucpr.carsrent.bookings.BookingRepository
import br.pucpr.carsrent.bookings._booking
import br.pucpr.carsrent.exceptions.BadRequestException
import io.kotest.matchers.collections.shouldHaveAtLeastSize
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import io.kotest.matchers.throwable.shouldHaveMessage
import io.mockk.checkUnnecessaryStub
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class VehicleServiceTest {
    private val vehicleRepository = mockk<VehicleRepository>()
    private val bookingRepository = mockk<BookingRepository>()

    private val service = VehicleService(vehicleRepository, bookingRepository)

    @BeforeEach
    fun setup() = clearAllMocks()

    @AfterEach
    fun cleanUp() = checkUnnecessaryStub()

    @Test
    fun `insert throws BadRequestException if an vehicle already booked`() {
        val vehicle = _vehicle(id = null)
        every { vehicleRepository.findByFilter(vehicle.brand, vehicle.model, vehicle.modelYear, vehicle.price) } returns _vehicle(id = 1)

        assertThrows<BadRequestException> {
            service.insert(vehicle)
        } shouldHaveMessage  "Vehicle already exists!"
    }

    @Test
    fun `insert must return the saved vehicle if it is inserted`() {
        val vehicle = _vehicle(id = null)
        every { vehicleRepository.findByFilter(vehicle.brand, vehicle.model, vehicle.modelYear, vehicle.price) } returns null

        val saved = _vehicle(id = 1)
        every { vehicleRepository.save(vehicle) } returns saved

        service.insert(vehicle) shouldBe saved
    }

    @Test
    fun `findAll should return all vehicles`() {
        val vehicles = mutableListOf(_vehicle(id = 1))

        every { service.findAll() } returns vehicles

        service.findAll() shouldHaveAtLeastSize 1
    }

    @Test
    fun `findByIdOrNull should return vehicle when vehicle exists`() {
        val vehicle = _vehicle(id = 1)

        every { service.findByIdOrNull(1) } returns vehicle

        service.findByIdOrNull(1) shouldBe vehicle
    }

    @Test
    fun `deleteById throws BadRequestException if the vehicle is booked`() {
        val bookings = listOf(_booking(id = 1))

        every { bookingRepository.findByVehicleId(1) } returns bookings

        assertThrows<BadRequestException> {
            service.deleteById(1)
        } shouldHaveMessage  "Vehicle has bookings!"
    }

    @Test
    fun `deleteById should delete vehicle when vehicle has no bookings`() {
        every { bookingRepository.findByVehicleId(1) } returns emptyList()
        every { vehicleRepository.deleteById(1) } returns Unit

        service.deleteById(1) shouldBe Unit
    }

    @Test
    fun `findByCategory should return vehicles of given category`() {
        val vehicles = mutableListOf(_vehicle(category = "SUV"), _vehicle(category = "SUV"))

        every { vehicleRepository.findByCategory("SUV") } returns vehicles

        service.findByCategory("SUV") shouldHaveSize 2
    }
}