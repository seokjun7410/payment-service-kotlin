package com.example.paymentsystemkotlin.payment.application.service

import com.example.paymentsystemkotlin.common.UseCase
import com.example.paymentsystemkotlin.payment.application.domain.PaymentConfirmationResult
import com.example.paymentsystemkotlin.payment.application.port.`in`.PaymentConfirmCommand
import com.example.paymentsystemkotlin.payment.application.port.`in`.PaymentConfirmUsecase
import com.example.paymentsystemkotlin.payment.application.port.out.PaymentExecutorPort
import com.example.paymentsystemkotlin.payment.application.port.out.PaymentStatusUpdateCommand
import com.example.paymentsystemkotlin.payment.application.port.out.PaymentStatusUpdatePort
import com.example.paymentsystemkotlin.payment.application.port.out.PaymentValidationPort
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@UseCase
class PaymentConfirmService(
        private val paymentStatusUpdatePort: PaymentStatusUpdatePort,
        private val paymentValidationPort: PaymentValidationPort,
        private val paymentExecutorPort: PaymentExecutorPort
) : PaymentConfirmUsecase{
    override fun confirm(command: PaymentConfirmCommand): Mono<PaymentConfirmationResult> {
        return paymentStatusUpdatePort.updatePaymentStatusToExecuting(command.orderId,command.paymentKey)
                .filterWhen { paymentValidationPort.isValid(command.orderId,command.amount) }
                .flatMap { paymentExecutorPort.execute(command)}
                .flatMap {
                    paymentStatusUpdatePort.updatePaymentStatus(
                            command = PaymentStatusUpdateCommand(
                                    paymentKey = it.paymentKey,
                                    orderId = it.orderId,
                                    status = it.paymentStatus(),
                                    extraDetails = it.extraDetails,
                                    failure = it.failure
                            )
                        ).thenReturn(it)
                }
                .map { PaymentConfirmationResult(status = it.paymentStatus(), failure = it.failure) }
    }
}