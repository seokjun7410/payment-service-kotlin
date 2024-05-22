package com.example.paymentsystemkotlin.payment.application.port.out

import com.example.paymentsystemkotlin.payment.application.domain.PaymentExecutionResult
import com.example.paymentsystemkotlin.payment.application.port.`in`.PaymentConfirmCommand
import reactor.core.publisher.Mono

interface PaymentExecutorPort {
    fun execute(command: PaymentConfirmCommand): Mono<PaymentExecutionResult>

}
