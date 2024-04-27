package br.pucpr.carsrent.users

import jakarta.validation.Valid
import jakarta.websocket.server.PathParam
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/users")
class UserController(
    val userService: UserService
) {
    @PostMapping
    fun insert(@Valid @RequestBody userRequest: UserRequest) = userService.save(userRequest.toUser())

    @GetMapping
    fun findAll() = userService.findAll()

    @GetMapping("/{id}")
    fun findByIdOrNull(@PathVariable id: Long): ResponseEntity<User> {
        val user = userService.findByIdOrNull(id) ?: return ResponseEntity.notFound().build()
        return ResponseEntity.ok(user)
    }
}