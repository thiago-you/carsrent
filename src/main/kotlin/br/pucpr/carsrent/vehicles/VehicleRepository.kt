package br.pucpr.carsrent.vehicles

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface VehicleRepository: JpaRepository<Vehicle, Long>{
    fun findByCategory(category: String): List<Vehicle>

    @Query(
        "select distinct v from Vehicle v where " +
                "v.brand = :brand and " +
                "v.model = :model and " +
                "v.modelYear = :modelYear and " +
                "v.price = :price"
    )
    fun findByFilter(brand: String, model: String, modelYear: Int, price: Double): Vehicle?
}