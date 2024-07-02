package com.example.paymentsystemkotlin.payment.application.domain

import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Component
import org.springframework.transaction.reactive.TransactionalEventPublisher
import reactor.core.publisher.Mono

//database에 성공적으로 커밋된 후에 이벤트를 발행하도록 만들기위해 Publisher
@Component
class PaymentEventMessagePublisher(
        publisher: ApplicationEventPublisher
){
    private val transactionalEventPublisher = TransactionalEventPublisher(publisher)

    fun publishEvent(paymentEventMessage: PaymentEventMessage): Mono<PaymentEventMessage> {

        //transaction commit까지 지연
        return transactionalEventPublisher.publishEvent(paymentEventMessage)
                .thenReturn(paymentEventMessage)
    }
}