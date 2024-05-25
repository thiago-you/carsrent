package br.pucpr.carsrent.vehicles.requests

import br.pucpr.carsrent.vehicles.Vehicle
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.*

data class VehicleRequest(
    @field:NotBlank
    var brand: String = "",
    @field:NotBlank
    var model: String = "",
    @field:Min(value = 1950)
    @field:Max(value = 2025)
    @Schema(type = "integer", format = "int32", example = "2020")
    var modelYear: Int = 0,
    var color: String = "",
    @field:NotBlank
    var category: String = "",
    @field:DecimalMax(value = "9999.99")
    @field:DecimalMin(value = "0.01")
    @Schema(type = "number", format = "double", example = "300.50")
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