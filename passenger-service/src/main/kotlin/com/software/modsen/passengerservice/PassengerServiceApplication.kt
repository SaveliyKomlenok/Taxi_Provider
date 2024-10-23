package com.software.modsen.passengerservice

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.client.discovery.EnableDiscoveryClient
import org.springframework.cloud.openfeign.EnableFeignClients

@SpringBootApplication
@EnableFeignClients
@EnableDiscoveryClient
open class PassengerServiceApplication

fun main(args: Array<String>) {
    runApplication<PassengerServiceApplication>(*args)
}