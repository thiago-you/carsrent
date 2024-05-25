package br.pucpr.carsrent.vehicles

import br.pucpr.carsrent.exceptions.BadRequestException
import org.slf4j.LoggerFactory
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class VehicleService(
    val vehicleRepository: VehicleRepository
) {
    fun insert(vehicle: Vehicle): Vehicle {
        if (vehicleRepository.findByFilter(vehicle.brand, vehicle.model, vehicle.modelYear, vehicle.price) != null) {
            throw BadRequestException("Vehicle already exists!")
        }

        return vehicleRepository.save(vehicle).also {
            log.info("User inserted! {}", it.id)
        }
    }

    fun findAll(): MutableList<Vehicle> = vehicleRepository.findAll()

    fun findByIdOrNull(id: Long) = vehicleRepository.findByIdOrNull(id)

    fun deleteById(id: Long) {
        vehicleRepository.deleteById(id)
    }

    fun findByCategory(category: String) = vehicleRepository.findByCategory(category)

    companion object {
        private val log = LoggerFactory.getLogger(VehicleService::class.java)
    }
}