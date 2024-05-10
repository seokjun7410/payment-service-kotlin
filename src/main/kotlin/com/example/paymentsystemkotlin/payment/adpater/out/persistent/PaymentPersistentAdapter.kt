package com.example.paymentsystemkotlin.payment.adpater.out.persistent

import com.example.paymentsystemkotlin.common.PersistentAdapter
import com.example.paymentsystemkotlin.payment.adpater.out.persistent.repository.PaymentRepository
import com.example.paymentsystemkotlin.payment.application.domain.PaymentEvent
import com.example.paymentsystemkotlin.payment.application.port.out.SavePaymentPort
import reactor.core.publisher.Mono

@PersistentAdapter
class PaymentPersistentAdapter(
        private val paymentRepository: PaymentRepository
): SavePaymentPort
{
    override fun save(paymentEvent: PaymentEvent): Mono<Void> {
        return paymentRepository.save(paymentEvent)
    }
}