package br.pucpr.carsrent.roles

import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service

@Service
class RoleService(
    val roleRepository: RoleRepository
) {
    fun insert(role: Role): Role = roleRepository.save(role)

    fun findAll(): List<Role> = roleRepository.findAll(Sort.by("name").ascending())

    fun findByName(name: String): Role? = roleRepository.findByName(name)

    fun findById(id: Long) = roleRepository.findById(id)

    fun deleteById(id: Long) = roleRepository.deleteById(id)
}