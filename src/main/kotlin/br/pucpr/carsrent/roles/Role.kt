package br.pucpr.carsrent.roles

import jakarta.persistence.*

@Entity
@Table(name = "tblRoles")
class Role(
    @Id
    @GeneratedValue
    var id: Long? = null,
    @Column(unique = true, nullable = false)
    var name: String,
    var description: String = ""
)