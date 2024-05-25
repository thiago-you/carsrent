package br.pucpr.carsrent.vehicles.requests

import br.pucpr.carsrent.vehicles.Vehicle
import jakarta.validation.constraints.NotBlank

data class VehicleRequest(
    @field:NotBlank
    var brand: String = "",
    @field:NotBlank
    var model: String = "",
    var modelYear: Int = 0,
    var color: String = "",
    @field:NotBlank
    var category: String = "",
    @field:NotBlank
    var price: Double = 0.0
) {
    fun toVehicle() = Vehicle(
        brand = brand,
        model = model,
        modelYear = modelYear,
        color = color,
        category = category,
        price = price
    )
}