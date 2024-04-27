package br.pucpr.carsrent.users

import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/users")
class UserController(
    val userService: UserService
) {
    @PostMapping
    fun insert(@Valid @RequestBody userRequest: UserRequest) = ResponseEntity.status(HttpStatus.CREATED)

    @GetMapping
    fun findAll() = userService.findAll()

    @GetMapping("/{id}")
    fun findByIdOrNull(@PathVariable id: Long): ResponseEntity<User> {
        val user = userService.findByIdOrNull(id) ?: return ResponseEntity.notFound().build()
        return ResponseEntity.ok(user)
    }

    @DeleteMapping("/{id}")
    fun deleteById(@PathVariable id: Long) = userService.deleteById(id)
}