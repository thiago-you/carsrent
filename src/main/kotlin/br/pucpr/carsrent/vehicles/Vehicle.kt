package br.pucpr.carsrent.vehicles

import jakarta.persistence.*

@Entity
@Table(name = "tblVehicles")
class Vehicle(
    @Id
    @GeneratedValue
    var id: Long? = null,
    @Column(unique = false, nullable = false)
    var brand: String = "",
    @Column(unique = false, nullable = false)
    var model: String = "",
    var modelYear: Int = 0,
    var color: String = "",
    var category: String = "",
    var price: Double = 0.0
)