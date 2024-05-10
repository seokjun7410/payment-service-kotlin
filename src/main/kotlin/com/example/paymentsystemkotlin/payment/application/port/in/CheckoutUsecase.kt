package com.example.paymentsystemkotlin.payment.application.port.`in`

import com.example.paymentsystemkotlin.payment.application.domain.CheckoutResult
import reactor.core.publisher.Mono

interface CheckoutUsecase {

    fun checkout(command: CheckoutCommand): Mono<CheckoutResult>
}