package br.pucpr.carsrent.vehicles

import br.pucpr.carsrent.vehicles.requests.VehicleRequest
import br.pucpr.carsrent.vehicles.responses.VehicleResponse
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/vehicles")
class VehicleController(
    val vehicleService: VehicleService
) {
    @PostMapping
    fun insert(@Valid @RequestBody vehicleRequest: VehicleRequest): ResponseEntity<VehicleResponse> = vehicleRequest.toVehicle()
        .let { vehicleService.insert(it) }
        .let { VehicleResponse(it) }
        .let { ResponseEntity.ok(it) }

    @GetMapping
    fun findAll() = vehicleService.findAll()
        .map { VehicleResponse(it) }
        .let { ResponseEntity.ok(it) }

    @GetMapping("/{id}")
    fun findById(@PathVariable id: Long): ResponseEntity<VehicleResponse> = vehicleService.findByIdOrNull(id)
        ?.let { VehicleResponse(it) }
        ?.let { ResponseEntity.ok(it) }
        ?: ResponseEntity.notFound().build()

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @SecurityRequirement(name="WebToken")
    fun deleteById(@PathVariable id: Long): ResponseEntity<Void> = vehicleService.deleteById(id)
        .let { ResponseEntity.ok().build() }

    @GetMapping("/category/{category}")
    fun findByCategory(@PathVariable category: String) = vehicleService.findByCategory(category)
        .map { VehicleResponse(it) }
        .let { ResponseEntity.ok(it) }
}