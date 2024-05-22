package com.example.paymentsystemkotlin.payment.adpater.out.web.toss.executor

import com.example.paymentsystemkotlin.payment.application.domain.PaymentConfirmationResult
import com.example.paymentsystemkotlin.payment.application.domain.PaymentExecutionResult
import com.example.paymentsystemkotlin.payment.application.port.`in`.PaymentConfirmCommand
import reactor.core.publisher.Mono

interface PaymentExecutor {

    fun execute(command: PaymentConfirmCommand): Mono<PaymentExecutionResult>
}