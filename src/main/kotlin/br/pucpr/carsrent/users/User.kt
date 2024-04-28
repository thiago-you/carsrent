package br.pucpr.carsrent.users

import jakarta.persistence.Entity
import jakarta.persistence.Id

@Entity
class User (
    @Id
    var id: Long? = null,
    var email: String = "",
    var password: String = "",
    var name: String = "",
)