package br.pucpr.carsrent.vehicles.requests

import io.kotest.matchers.shouldBe
import io.mockk.checkUnnecessaryStub
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test

class VehicleRequestTest {

    @AfterEach
    fun cleanUp() = checkUnnecessaryStub()

    @Test
    fun `toVehicle should return Vehicle with same properties`() {
        val vehicleRequest = VehicleRequest(
            brand = "Toyota",
            model = "Corolla",
            modelYear = 2020,
            color = "Blue",
            category = "Sedan",
            price = 20000.0
        )

        val vehicle = vehicleRequest.toVehicle()

        vehicleRequest.brand shouldBe vehicle.brand
        vehicleRequest.model shouldBe vehicle.model
        vehicleRequest.modelYear shouldBe vehicle.modelYear
        vehicleRequest.color shouldBe vehicle.color
        vehicleRequest.category shouldBe vehicle.category
        vehicleRequest.price shouldBe vehicle.price
    }

    @Test
    fun `toVehicle should return Vehicle with default properties when properties are not set`() {
        val vehicleRequest = VehicleRequest()

        val vehicle = vehicleRequest.toVehicle()

        vehicleRequest.brand shouldBe vehicle.brand
        vehicleRequest.model shouldBe vehicle.model
        vehicleRequest.modelYear shouldBe vehicle.modelYear
        vehicleRequest.color shouldBe vehicle.color
        vehicleRequest.category shouldBe vehicle.category
        vehicleRequest.price shouldBe vehicle.price
    }
}