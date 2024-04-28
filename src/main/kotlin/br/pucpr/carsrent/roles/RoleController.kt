package br.pucpr.carsrent.roles

import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

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