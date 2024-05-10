package com.example.paymentsystemkotlin.payment.application.port.out

import com.example.paymentsystemkotlin.payment.application.domain.PaymentEvent
import reactor.core.publisher.Mono

interface SavePaymentPort {

    fun save(paymentEvent: PaymentEvent): Mono<Void>
}
