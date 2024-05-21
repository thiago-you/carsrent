package br.pucpr.carsrent

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication

@SpringBootApplication
@ConfigurationPropertiesScan
class CarsrentApplication

fun main(args: Array<String>) {
	runApplication<CarsrentApplication>(*args)
}
