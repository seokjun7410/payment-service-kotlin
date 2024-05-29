package com.example.paymentsystemkotlin.payment.application.port.out

import com.example.paymentsystemkotlin.payment.application.domain.PendingPaymentEvent
import reactor.core.publisher.Flux

interface LoadPendingPaymentPort {
    fun getPendingPayments(): Flux<PendingPaymentEvent>
}
