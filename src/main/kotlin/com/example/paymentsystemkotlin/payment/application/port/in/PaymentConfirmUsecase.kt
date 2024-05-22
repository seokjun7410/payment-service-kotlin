package com.example.paymentsystemkotlin.payment.application.port.`in`

import com.example.paymentsystemkotlin.payment.application.domain.PaymentConfirmationResult
import reactor.core.publisher.Mono

interface  PaymentConfirmUsecase {

    fun confirm(command: PaymentConfirmCommand) : Mono<PaymentConfirmationResult>;
}