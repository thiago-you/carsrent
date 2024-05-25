package br.pucpr.carsrent.vehicles.responses

import br.pucpr.carsrent.vehicles.Vehicle

data class VehicleResponse(
    val id: Long,
    val name: String,
    val category: String,
    val value: Double
) {
    constructor(vehicle: Vehicle) : this(
        id = vehicle.id!!,
        name = "%s %s - %d, %s".format(vehicle.brand, vehicle.model, vehicle.modelYear, vehicle.color),
        category = vehicle.category,
        value = vehicle.price
    )
}