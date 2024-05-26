package br.pucpr.carsrent.vehicles

import br.pucpr.carsrent.vehicles.requests.VehicleRequest

fun _vehicle(id: Long? = null, brand: String = "Toyota", model: String = "Corolla", category: String = "Sedan", price: Double = 300.0) = Vehicle(
    id = id,
    brand = brand,
    model = model,
    category = category,
    price = price
)