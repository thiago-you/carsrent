package br.pucpr.carsrent.vehicles.responses

import br.pucpr.carsrent.vehicles.Vehicle
import io.kotest.matchers.shouldBe
import io.mockk.checkUnnecessaryStub
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test

class VehicleResponseTest {
    companion object {
        private const val NAME = "%s %s - %d, %s"
    }

    @AfterEach
    fun cleanUp() = checkUnnecessaryStub()

    @Test
    fun `constructor should create VehicleResponse with same properties from Vehicle`() {
        val vehicle = Vehicle(
            id = 1L,
            brand = "Toyota",
            model = "Corolla",
            modelYear = 2020,
            color = "Blue",
            category = "Sedan",
            price = 100.0
        )

        val vehicleResponse = VehicleResponse(vehicle)

        vehicle.id shouldBe vehicleResponse.id
        vehicle.category shouldBe vehicleResponse.category
        vehicle.price shouldBe vehicleResponse.value
        NAME.format(vehicle.brand, vehicle.model, vehicle.modelYear, vehicle.color) shouldBe vehicleResponse.name
    }
}