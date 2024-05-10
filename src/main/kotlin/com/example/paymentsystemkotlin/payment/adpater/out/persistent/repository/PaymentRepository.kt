package com.example.paymentsystemkotlin.payment.adpater.out.persistent.repository

import com.example.paymentsystemkotlin.payment.application.domain.PaymentEvent
import reactor.core.publisher.Mono


interface PaymentRepository{
    fun save(paymentEvent: PaymentEvent): Mono<Void>
}