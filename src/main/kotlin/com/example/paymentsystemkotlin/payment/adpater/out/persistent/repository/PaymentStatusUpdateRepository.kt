package com.example.paymentsystemkotlin.payment.adpater.out.persistent.repository

import com.example.paymentsystemkotlin.payment.application.port.out.PaymentStatusUpdateCommand
import reactor.core.publisher.Mono

interface PaymentStatusUpdateRepository {
    fun updatePaymentStatusToExecuting(orderId:String, paymentKey: String): Mono<Boolean>
    fun updatePaymentStatus(command: PaymentStatusUpdateCommand): Mono<Boolean>
}