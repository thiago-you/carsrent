package br.pucpr.carsrent.bookings

import br.pucpr.carsrent.bookings.requests.BookingRequest
import br.pucpr.carsrent.bookings.responses.BookingResponse
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/bookings")
class BookingController(
    val bookingService: BookingService
) {
    @PostMapping
    fun insert(@Valid @RequestBody bookingRequest: BookingRequest): ResponseEntity<BookingResponse> = bookingRequest.toBooking()
        .let { bookingService.insert(it) }
        .let { BookingResponse(it) }
        .let { ResponseEntity.ok(it) }

    @GetMapping
    fun findAll() = bookingService.findAll()
        .map { BookingResponse(it) }
        .let { ResponseEntity.ok(it) }

    @GetMapping("/{id}")
    fun findById(@PathVariable id: Long): ResponseEntity<BookingResponse> = bookingService.findByIdOrNull(id)
        ?.let { BookingResponse(it) }
        ?.let { ResponseEntity.ok(it) }
        ?: ResponseEntity.notFound().build()

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @SecurityRequirement(name="WebToken")
    fun deleteById(@PathVariable id: Long): ResponseEntity<Void> = bookingService.deleteById(id)
        .let { ResponseEntity.ok().build() }

    @GetMapping("/vehicle/{vehicleId}")
    fun findByVehicleId(@PathVariable vehicleId: Long) = bookingService.findByVehicleId(vehicleId)
        .map { BookingResponse(it) }
        .let { ResponseEntity.ok(it) }

    @GetMapping("/user/{userId}")
    fun findByUserId(@PathVariable userId: Long) = bookingService.findByUserId(userId)
        .map { BookingResponse(it) }
        .let { ResponseEntity.ok(it) }

    @GetMapping("/user/{status}")
    fun findByStatus(@PathVariable status: String) = bookingService.findByStatus(status)
        .map { BookingResponse(it) }
        .let { ResponseEntity.ok(it) }

    @PutMapping("/{bookingId}/vehicle/{vehicleId}")
    fun updateBookingVehicle(@PathVariable bookingId: Long, @PathVariable vehicleId: Long) = bookingService.updateVehicle(bookingId, vehicleId)
        ?.let { BookingResponse(it) }
        ?.let { ResponseEntity.ok(it) }

    @PutMapping("/{bookingId}/cancel")
    @SecurityRequirement(name="WebToken")
    fun cancelBooking(@PathVariable bookingId: Long): ResponseEntity<Void> = bookingService.cancelBooking(bookingId)
        .let { ResponseEntity.ok().build() }

    @PutMapping("/{bookingId}/close")
    @PreAuthorize("hasRole('ADMIN')")
    @SecurityRequirement(name="WebToken")
    fun closeBooking(@PathVariable bookingId: Long): ResponseEntity<Void> = bookingService.closeBooking(bookingId)
        .let { ResponseEntity.ok().build() }
}