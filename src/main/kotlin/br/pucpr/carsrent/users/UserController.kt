package br.pucpr.carsrent.users

import br.pucpr.carsrent.users.requests.LoginRequest
import br.pucpr.carsrent.users.requests.UserRequest
import br.pucpr.carsrent.users.responses.LoginResponse
import br.pucpr.carsrent.users.responses.UserResponse
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
    fun insert(@Valid @RequestBody userRequest: UserRequest): ResponseEntity<UserResponse> = userRequest.toUser()
        .let { userService.save(it) }
        .let { UserResponse(it) }
        .let { ResponseEntity.status(HttpStatus.CREATED).body(it) }

    @GetMapping
    fun findAll(@RequestParam sortDir: String? = null, @RequestParam role: String?) = SortDir.byName(sortDir)
        .let { userService.findAll(it, role) }
        .map { UserResponse(it) }
        .let { ResponseEntity.ok(it) }

    @GetMapping("/{id}")
    fun findById(@PathVariable id: Long): ResponseEntity<UserResponse> = userService.findByIdOrNull(id)
        ?.let { UserResponse(it) }
        ?.let { ResponseEntity.ok(it) }
        ?: ResponseEntity.notFound().build()

    @DeleteMapping("/{id}")
    fun deleteById(@PathVariable id: Long): ResponseEntity<Void> = userService.deleteById(id)
        .let { ResponseEntity.ok().build() }

    @PutMapping("/{id}/roles/{role}")
    fun grant(
        @PathVariable id: Long,
        @PathVariable role: String
    ): ResponseEntity<Void> = userService.addRole(id, role).takeIf { it }
        ?.let { ResponseEntity.ok().build() }
        ?: ResponseEntity.noContent().build()

    @PostMapping("/login")
    fun login(@Valid @RequestBody loginRequest: LoginRequest): ResponseEntity<LoginResponse> =
        userService.login(loginRequest.email!!, loginRequest.password!!)
            ?.let { ResponseEntity.ok(it) }
            ?: ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()
}