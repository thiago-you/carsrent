package br.pucpr.carsrent.users

import org.springframework.data.domain.Sort
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class UserService(
    val userRepository: UserRepository
) {
    fun save(user: User) = userRepository.save(user)

    fun findAll(sortDir: SortDir): MutableList<User> = when (sortDir) {
        SortDir.ASC -> userRepository.findAll(Sort.by("name").ascending())
        SortDir.DESC -> userRepository.findAll(Sort.by("name").descending())
    }

    fun findByIdOrNull(id: Long) = userRepository.findByIdOrNull(id)

    fun deleteById(id: Long) = userRepository.deleteById(id)
}