package com.example.paymentsystemkotlin.payment.adpater.out.persistent

import com.example.paymentsystemkotlin.common.PersistentAdapter
import com.example.paymentsystemkotlin.payment.adpater.out.persistent.repository.PaymentRepository
import com.example.paymentsystemkotlin.payment.adpater.out.persistent.repository.PaymentStatusUpdateRepository
import com.example.paymentsystemkotlin.payment.adpater.out.persistent.repository.PaymentValidationRepository
import com.example.paymentsystemkotlin.payment.application.domain.PaymentEvent
import com.example.paymentsystemkotlin.payment.application.port.out.PaymentStatusUpdateCommand
import com.example.paymentsystemkotlin.payment.application.port.out.PaymentStatusUpdatePort
import com.example.paymentsystemkotlin.payment.application.port.out.PaymentValidationPort
import com.example.paymentsystemkotlin.payment.application.port.out.SavePaymentPort
import reactor.core.publisher.Mono

@PersistentAdapter
class PaymentPersistentAdapter(
        private val paymentRepository: PaymentRepository,
        private val paymentStatusUpdateRepository: PaymentStatusUpdateRepository,
        private val paymentValidationRepository: PaymentValidationRepository
): SavePaymentPort, PaymentStatusUpdatePort, PaymentValidationPort
{
    override fun save(paymentEvent: PaymentEvent): Mono<Void> {
        return paymentRepository.save(paymentEvent)
    }

    override fun updatePaymentStatusToExecuting(orderId: String, paymentKey: String): Mono<Boolean> {
        return paymentStatusUpdateRepository.updatePaymentStatusToExecuting(orderId,paymentKey)
    }

    override fun updatePaymentStatus(command: PaymentStatusUpdateCommand): Mono<Boolean> {
        return paymentStatusUpdateRepository.updatePaymentStatus(command)
    }

    override fun isValid(orderId: String, amount: Long): Mono<Boolean> {
        return paymentValidationRepository.isValid(orderId,amount)
    }
}