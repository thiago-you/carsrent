package br.pucpr.carsrent.users

import org.springframework.stereotype.Service

@Service
class UserService(
    val userRepository: UserRepository
) {
    fun save(user: User) = userRepository.save(user)

    fun findAll(sortDir: SortDir) = userRepository.findAll(sortDir)

    fun findByIdOrNull(id: Long) = userRepository.findByIdOrNull(id)

    fun deleteById(id: Long) = userRepository.deleteById(id)
}