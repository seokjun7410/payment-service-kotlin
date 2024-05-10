package com.example.paymentsystemkotlin.payment.test

import com.example.paymentsystemkotlin.payment.application.domain.PaymentEvent
import reactor.core.publisher.Mono

interface PaymentDatabaseHelper {
    fun getPayments(orderId: String): PaymentEvent?
    fun clean():Mono<Void>
}
