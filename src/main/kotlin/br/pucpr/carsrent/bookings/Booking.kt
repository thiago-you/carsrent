package br.pucpr.carsrent.bookings

import br.pucpr.carsrent.users.User
import br.pucpr.carsrent.vehicles.Vehicle
import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*

@Entity
@Table(name = "tblBookings")
class Booking(
    @Id
    @GeneratedValue
    var id: Long? = null,
    @Column(nullable = false)
    var days: Int = 0,
    @Column(nullable = false)
    var status: String = "",
    @Column(nullable = false)
    var totalPrice: Double = 0.0,
    @JsonIgnore
    @ManyToOne
    @JoinTable(
        name = "BookingVehicle",
        joinColumns = [JoinColumn(name = "idBooking")],
        inverseJoinColumns = [JoinColumn(name = "idVehicle")]
    )
    var vehicle: Vehicle? = null,
    @JsonIgnore
    @ManyToOne
    @JoinTable(
        name = "BookingUser",
        joinColumns = [JoinColumn(name = "idBooking")],
        inverseJoinColumns = [JoinColumn(name = "idUser")]
    )
    var user: User? = null,
)