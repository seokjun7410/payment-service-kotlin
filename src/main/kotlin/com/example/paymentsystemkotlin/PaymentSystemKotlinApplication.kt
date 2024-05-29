package com.example.paymentsystemkotlin

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
@EnableScheduling
class PaymentSystemKotlinApplication

fun main(args: Array<String>) {
    runApplication<PaymentSystemKotlinApplication>(*args)
}
