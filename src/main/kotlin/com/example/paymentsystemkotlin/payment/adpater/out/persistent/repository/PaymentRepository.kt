package com.example.paymentsystemkotlin.payment.adpater.out.persistent.repository

import com.example.paymentsystemkotlin.payment.application.domain.PaymentEvent
import com.example.paymentsystemkotlin.payment.application.domain.PendingPaymentEvent
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono


interface PaymentRepository{
    fun save(paymentEvent: PaymentEvent): Mono<Void>
    fun getPendingPayments(): Flux<PendingPaymentEvent>
}