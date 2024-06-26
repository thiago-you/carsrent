package br.pucpr.carsrent.users

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface UserRepositorys: JpaRepository<Users, Long> {
    fun findByEmail(email: String): Users?

    @Query(
        "select distinct u from User u join u.roles r where r.name = :role order by u.name"
    )
    fun findByRole(role: String): List<Users>
}