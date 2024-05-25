package br.pucpr.carsrent.roles

import br.pucpr.carsrent.roles.requests.RoleRequest
import br.pucpr.carsrent.roles.responses.RoleResponse
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/roles")
class RoleController(
    val roleService: RoleService
) {
    @PostMapping
    fun insert(@Valid @RequestBody roleRequest: RoleRequest): ResponseEntity<RoleResponse> = roleRequest.toRole()
        .let { roleService.insert(it) }
        .let { RoleResponse(it) }
        .let { ResponseEntity.status(HttpStatus.CREATED).body(it) }

    @GetMapping
    fun findAll() = roleService.findAll()
        .map { RoleResponse(it) }
        .let { ResponseEntity.ok(it) }
}