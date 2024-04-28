package br.pucpr.carsrent.users

import br.pucpr.carsrent.roles.Role
import jakarta.persistence.*
import org.jetbrains.annotations.NotNull

@Entity
@Table(name = "tblUsers")
class User(
    @Id
    @GeneratedValue
    var id: Long? = null,
    @Column(unique = true, nullable = false)
    var email: String = "",
    @NotNull
    var password: String = "",
    @NotNull
    var name: String = "",
    @ManyToMany
    @JoinTable(
        name = "UserRoles",
        joinColumns = [JoinColumn(name = "idUser")],
        inverseJoinColumns = [JoinColumn(name = "idRole")]
    )
    var roles: MutableSet<Role> = mutableSetOf()
)