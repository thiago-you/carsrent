package br.pucpr.carsrent.bookings

import br.pucpr.carsrent.exceptions.BadRequestException
import br.pucpr.carsrent.security.AuthUser
import br.pucpr.carsrent.security.Jwt
import br.pucpr.carsrent.users.UserRepository
import br.pucpr.carsrent.users._user
import br.pucpr.carsrent.vehicles.VehicleRepository
import br.pucpr.carsrent.vehicles._vehicle
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContainIgnoringCase
import io.mockk.*
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.data.repository.findByIdOrNull

class BookingServiceTest {
    private val vehicleRepository = mockk<VehicleRepository>()
    private val bookingRepository = mockk<BookingRepository>()
    private val userRepository = mockk<UserRepository>()
    private val authUser = mockk<AuthUser>()

    private val service = BookingService(bookingRepository, vehicleRepository, userRepository, authUser)

    @BeforeEach
    fun setup() {
        clearAllMocks()
        mockkStatic(Jwt::class)
    }

    @AfterEach
    fun cleanUp() {
        unmockkAll()
        checkUnnecessaryStub()
    }

    @Test
    fun `insert throws BadRequestException if vehicle is already booked`() {
        val vehicle = _vehicle(id = 1, price = 100.0)
        val booking = _booking(vehicle = vehicle)

        every { bookingRepository.findByVehicleId(vehicle.id!!) } returns listOf(booking)

        shouldThrow<BadRequestException> {
            service.insert(booking)
        }.message shouldContainIgnoringCase "Vehicle already booked!"
    }

    @Test
    fun `insert throws BadRequestException if vehicle not found`() {
        val vehicle = _vehicle(id = 1, price = 100.0)
        val booking = _booking(vehicle = vehicle)

        every { bookingRepository.findByVehicleId(vehicle.id!!) } returns emptyList()
        every { vehicleRepository.findByIdOrNull(vehicle.id) } returns null

        shouldThrow<BadRequestException> {
            service.insert(booking)
        }.message shouldContainIgnoringCase "Vehicle not found!"
    }

    @Test
    fun `insert throws BadRequestException if user not found`() {
        val user = _user(id = 1)
        val vehicle = _vehicle(id = 1, price = 100.0)
        val booking = _booking(vehicle = vehicle, user = user)

        every { bookingRepository.findByVehicleId(vehicle.id!!) } returns emptyList()
        every { vehicleRepository.findByIdOrNull(vehicle.id) } returns vehicle
        every { userRepository.findByIdOrNull(user.id) } returns null

        shouldThrow<BadRequestException> {
            service.insert(booking)
        }.message shouldContainIgnoringCase "User not found!"
    }

    @Test
    fun `insert saves and returns the booking if valid`() {
        val user = _user(id = 1)
        val vehicle = _vehicle(id = 1, price = 100.0)
        val booking = _booking(vehicle = vehicle, user = user, days = 2)

        val saved = _booking(id = 1, vehicle = vehicle, user = user, days = 2, totalPrice = 200.0)

        every { bookingRepository.findByVehicleId(vehicle.id!!) } returns emptyList()
        every { vehicleRepository.findByIdOrNull(vehicle.id) } returns vehicle
        every { userRepository.findByIdOrNull(user.id) } returns user
        every { bookingRepository.save(booking) } returns saved

        val result = service.insert(booking)

        result shouldBe saved
        result.status shouldBe BookingStatus.OPEN.toString()
        result.totalPrice shouldBe saved.days * vehicle.price
    }

    @Test
    fun `updateVehicle throws BadRequestException if booking not found`() {
        val vehicle = _vehicle(id = 1)
        val booking = _booking(id = 1)

        every { bookingRepository.findByIdOrNull(vehicle.id) } returns null

        shouldThrow<BadRequestException> {
            service.updateVehicle(booking.id!!, vehicle.id!!)
        }.message shouldContainIgnoringCase "Booking not found!"
    }

    @Test
    fun `updateVehicle throws BadRequestException if vehicle not found`() {
        val vehicle = _vehicle(id = 1)
        val booking = _booking(id = 1, vehicle = vehicle, status = BookingStatus.OPEN)

        every { bookingRepository.findByIdOrNull(booking.id) } returns booking
        every { vehicleRepository.findByIdOrNull(vehicle.id) } returns null

        shouldThrow<BadRequestException> {
            service.updateVehicle(booking.id!!, vehicle.id!!)
        }.message shouldContainIgnoringCase "Vehicle not found!"
    }

    @Test
    fun `updateVehicle throws BadRequestException if vehicle already added`() {
        val vehicle = _vehicle(id = 1)
        val booking = _booking(id = 1, vehicle = vehicle, status = BookingStatus.OPEN)

        every { bookingRepository.findByIdOrNull(booking.id) } returns booking
        every { vehicleRepository.findByIdOrNull(vehicle.id) } returns vehicle

        shouldThrow<BadRequestException> {
            service.updateVehicle(booking.id!!, vehicle.id!!)
        }.message shouldContainIgnoringCase "Vehicle already added to this booking!"
    }

    @Test
    fun `updateVehicle throws BadRequestException if booking not available for update`() {
        val vehicle = _vehicle(id = 1)
        val newVehicle = _vehicle(id = 2)
        val booking = _booking(id = 1, vehicle = vehicle, status = BookingStatus.CONFIRMED)

        every { bookingRepository.findByIdOrNull(booking.id) } returns booking
        every { vehicleRepository.findByIdOrNull(newVehicle.id) } returns newVehicle

        shouldThrow<BadRequestException> {
            service.updateVehicle(1L, 2L)
        }.message shouldContainIgnoringCase "Booking not available for update!"
    }

    @Test
    fun `updateVehicle updates the booking`() {
        val vehicle = _vehicle(id = 1)
        val newVehicle = _vehicle(id = 2)
        val booking = _booking(id = 1, vehicle = vehicle, status = BookingStatus.OPEN)

        val saved = _booking(id = 1, vehicle = newVehicle, status = BookingStatus.OPEN)

        every { bookingRepository.findByIdOrNull(booking.id) } returns booking
        every { vehicleRepository.findByIdOrNull(newVehicle.id) } returns newVehicle
        every { bookingRepository.save(any()) } returns saved

        val result = service.updateVehicle(booking.id!!, newVehicle.id!!)

        result shouldBe saved
        result?.vehicle shouldBe newVehicle
    }

    @Test
    fun `updateBookingStatus throws BadRequestException if booking not found`() {
        every { bookingRepository.findByIdOrNull(any()) } returns null

        shouldThrow<BadRequestException> {
            service.updateBookingStatus(1, BookingStatus.CONFIRMED)
        }.message shouldContainIgnoringCase "Booking not found!"
    }

    @Test
    fun `updateBookingStatus throws BadRequestException if booking not available`() {
        val booking = _booking(id = 1, status = BookingStatus.CONFIRMED)

        every { bookingRepository.findByIdOrNull(booking.id) } returns booking

        shouldThrow<BadRequestException> {
            service.updateBookingStatus(booking.id!!, BookingStatus.CONFIRMED)
        }.message shouldContainIgnoringCase "Booking not available!"
    }

    @Test
    fun `updateBookingStatus throws BadRequestException if user not allowed to cancel booking`() {
        val user = _user(id = 2)
        val booking = _booking(id = 1, status = BookingStatus.OPEN, user = user)

        every { bookingRepository.findByIdOrNull(booking.id) } returns booking
        every { authUser.getId() } returns 1

        shouldThrow<BadRequestException> {
            service.updateBookingStatus(1L, BookingStatus.CANCELED)
        }.message shouldContainIgnoringCase "User not allowed to cancel this booking!"
    }

    @Test
    fun `updateBookingStatus cancel booking with booking user`() {
        val user = _user(id = 1)
        val booking = _booking(id = 1, status = BookingStatus.OPEN, user = user)

        every { bookingRepository.findByIdOrNull(booking.id) } returns booking
        every { bookingRepository.save(booking) } returns booking
        every { authUser.getId() } returns 1

        service.updateBookingStatus(booking.id!!, BookingStatus.CANCELED) shouldBe Unit
    }

    @Test
    fun `updateBookingStatus confirm booking`() {
        val user = _user(id = 1)
        val booking = _booking(id = 1, status = BookingStatus.OPEN, user = user)

        every { bookingRepository.findByIdOrNull(booking.id) } returns booking
        every { bookingRepository.save(booking) } returns booking

        service.updateBookingStatus(booking.id!!, BookingStatus.CONFIRMED) shouldBe Unit
    }

    @Test
    fun `findAll returns all bookings`() {
        val bookings = listOf(_booking(id = 1), _booking(id = 2))
        every { bookingRepository.findAll() } returns bookings

        service.findAll() shouldBe bookings
    }

    @Test
    fun `findByIdOrNull returns booking if found`() {
        val booking = Booking(id = 1)
        every { bookingRepository.findByIdOrNull(1) } returns booking

        service.findByIdOrNull(1) shouldBe booking
    }

    @Test
    fun `findByIdOrNull returns null if not found`() {
        every { bookingRepository.findByIdOrNull(1) } returns null
        service.findByIdOrNull(1) shouldBe null
    }

    @Test
    fun `deleteById deletes the booking`() {
        every { bookingRepository.deleteById(1) } returns Unit
        service.deleteById(1) shouldBe Unit
    }

    @Test
    fun `findByVehicleId returns bookings by vehicle ID`() {
        val bookings = listOf(_booking(id = 1), _booking(id = 2))
        every { bookingRepository.findByVehicleId(1) } returns bookings

        service.findByVehicleId(1L) shouldBe bookings
    }

    @Test
    fun `findByUserId returns bookings by user ID`() {
        val bookings = listOf(_booking(id = 1), _booking(id = 2))
        every { bookingRepository.findByUserId(1) } returns bookings

        service.findByUserId(1) shouldBe bookings
    }

    @Test
    fun `findByStatus returns bookings by status`() {
        val bookings = listOf(_booking(id = 1), _booking(id = 2))
        every { bookingRepository.findByStatus(BookingStatus.OPEN.toString()) } returns bookings

        service.findByStatus(BookingStatus.OPEN) shouldBe bookings
    }
}