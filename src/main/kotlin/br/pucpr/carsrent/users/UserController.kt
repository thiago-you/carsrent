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
    fun insert(@Valid @RequestBody userRequest: UserRequest): ResponseEntity<User> = userRequest.toUser()
        .let { userService.save(it) }
        .let { ResponseEntity.status(HttpStatus.CREATED).body(it) }

    @GetMapping
    fun findAll(sortDir: String? = null) = SortDir.byName(sortDir)
        ?.let { userService.findAll(it) }
        ?.let { ResponseEntity.ok(it) }
        ?: ResponseEntity.badRequest().build()

    @GetMapping("/{id}")
    fun findById(@PathVariable id: Long): ResponseEntity<User> = userService.findByIdOrNull(id)
            ?.let { ResponseEntity.ok(it) }
            ?: ResponseEntity.notFound().build()

    @DeleteMapping("/{id}")
    fun deleteById(@PathVariable id: Long): ResponseEntity<Void> =
        userService.deleteById(id)
            ?.let { ResponseEntity.ok().build() }
            ?: ResponseEntity.notFound().build()
}