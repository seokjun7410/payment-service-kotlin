package com.example.paymentsystemkotlin.payment.adpater.out.persistent.repository

import com.example.paymentsystemkotlin.payment.application.domain.PaymentEventMessage
import com.example.paymentsystemkotlin.payment.application.domain.PaymentEventMessageType
import com.example.paymentsystemkotlin.payment.application.port.out.PaymentStatusUpdateCommand
import reactor.core.publisher.Mono

interface PaymentOutboxRepository
{
    fun insertOutbox(command: PaymentStatusUpdateCommand): Mono<PaymentEventMessage>
    fun markMessageAsSent(idempotencyKey:String, type: PaymentEventMessageType): Mono<Boolean>
    fun markMessageAsFailure(idempotencyKey:String, type: PaymentEventMessageType): Mono<Boolean>
}