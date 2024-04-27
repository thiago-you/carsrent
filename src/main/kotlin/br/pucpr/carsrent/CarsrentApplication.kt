package br.pucpr.carsrent

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class CarsrentApplication

fun main(args: Array<String>) {
	runApplication<CarsrentApplication>(*args)
}
