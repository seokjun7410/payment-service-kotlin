package com.example.paymentsystemkotlin.payment.adpater.out.web.toss

import com.example.paymentsystemkotlin.common.WebAdapter
import com.example.paymentsystemkotlin.payment.adpater.out.web.toss.executor.PaymentExecutor
import com.example.paymentsystemkotlin.payment.application.domain.PaymentExecutionResult
import com.example.paymentsystemkotlin.payment.application.port.`in`.PaymentConfirmCommand
import com.example.paymentsystemkotlin.payment.application.port.out.PaymentExecutorPort
import reactor.core.publisher.Mono

@WebAdapter
class PaymentExecutorAdapter(
        private val paymentExecutor: PaymentExecutor
) : PaymentExecutorPort {
    override fun execute(command: PaymentConfirmCommand): Mono<PaymentExecutionResult> {
        return paymentExecutor.execute(command)
    }

}