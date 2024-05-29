package com.example.paymentsystemkotlin.payment.application.service

import com.example.paymentsystemkotlin.payment.application.port.`in`.PaymentConfirmCommand
import com.example.paymentsystemkotlin.payment.application.port.`in`.PaymentRecoveryUsecase
import com.example.paymentsystemkotlin.payment.application.port.out.*
import org.springframework.scheduling.annotation.Scheduled
import reactor.core.scheduler.Schedulers
import java.util.concurrent.TimeUnit

class PaymentRecoveryService(
        private val loadPendingPaymentPort: LoadPendingPaymentPort,
        private val paymentValidationPort: PaymentValidationPort,
        private val paymentExecutorPort: PaymentExecutorPort,
        private val paymentStatusUpdatePort: PaymentStatusUpdatePort,
        private val paymentErrorHandler: PaymentErrorHandler
): PaymentRecoveryUsecase{

    private val scheduler = Schedulers.newSingle("recovery")
    @Scheduled(fixedDelay = 180, initialDelay = 180, timeUnit = TimeUnit.SECONDS)
    override fun recovery() {
        loadPendingPaymentPort.getPendingPayments()
                .map {
                    PaymentConfirmCommand(
                            paymentKey = it.paymentKey,
                            orderId = it.orderId,
                            amount = it.totalAmount()
                    )
                }
                .parallel(2)
                .runOn(Schedulers.parallel())
                .flatMap { command ->
                    paymentValidationPort.isValid(command.orderId, command.amount).thenReturn(command)
                            .flatMap { paymentExecutorPort.execute(it) }
                            .flatMap { paymentStatusUpdatePort.updatePaymentStatus(PaymentStatusUpdateCommand(it))  }
                            .onErrorResume { paymentErrorHandler.handlePaymentConfirmationError(it, command).thenReturn(true) }
                }
                .sequential()
                .subscribeOn(scheduler)
                .subscribe()
    }
}