package com.example.paymentsystemkotlin.payment.adpater.out.stream

import com.example.paymentsystemkotlin.common.Logger
import com.example.paymentsystemkotlin.common.StreamAdapter
import com.example.paymentsystemkotlin.payment.adpater.out.persistent.repository.PaymentOutboxRepository
import com.example.paymentsystemkotlin.payment.application.domain.PaymentEventMessage
import com.example.paymentsystemkotlin.payment.application.domain.PaymentEventMessageType
import jakarta.annotation.PostConstruct
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.integration.IntegrationMessageHeaderAccessor
import org.springframework.integration.annotation.ServiceActivator
import org.springframework.integration.channel.FluxMessageChannel
import org.springframework.kafka.support.KafkaHeaders
import org.springframework.messaging.Message
import org.springframework.messaging.support.MessageBuilder
import org.springframework.transaction.event.TransactionPhase
import org.springframework.transaction.event.TransactionalEventListener
import reactor.core.publisher.Flux
import reactor.core.publisher.Sinks
import reactor.core.scheduler.Schedulers
import reactor.kafka.sender.SenderResult
import java.util.function.Supplier

@Configuration
@StreamAdapter
class PaymentEventMessageSender(
    private val paymentOutboxRepository: PaymentOutboxRepository
) {

    //특정 Type의 data를 동적으로 발행하기 위해 Sinks 사용
    private val sender = Sinks.many().unicast().onBackpressureBuffer<Message<PaymentEventMessage>>()
    private val sendResult = Sinks.many().unicast().onBackpressureBuffer<SenderResult<String>>()

    @Bean
    fun send(): Supplier<Flux<Message<PaymentEventMessage>>> {
        return Supplier{
            sender.asFlux()
                    .onErrorContinue { err, _ ->
                        Logger.error("sendEventMessage",err.message ?: "failed to send eventMessage", err)
                    }
        }
    }

    @Bean(name = ["payment-result"])
    fun sendResultChannel(): FluxMessageChannel{
        return FluxMessageChannel()
    }

    @ServiceActivator(inputChannel = "payment-result")
    fun receiveSendResult(result: SenderResult<String>){
        if(result.exception() != null){ //message 전송 실패
                Logger.error("sendEventMessage", result.exception().message ?: "receive an esception for event message send.", result.exception())
        }
        sendResult.emitNext(result,Sinks.EmitFailureHandler.FAIL_FAST)
    }

    @PostConstruct
    fun handleSendResult(){
        sendResult.asFlux()
            .flatMap {
                when(it.recordMetadata() != null){
                    true -> paymentOutboxRepository.markMessageAsSent(it.correlationMetadata(),PaymentEventMessageType.PAYMENT_CONFIRMATION_SUCCESS)
                        false ->paymentOutboxRepository.markMessageAsFailure(it.correlationMetadata(),PaymentEventMessageType.PAYMENT_CONFIRMATION_SUCCESS)
                }
            }
            .onErrorContinue { err, _ -> Logger.error("sendEventMessage", err.message ?: "failed to mark the outbox message.", err)  }
            .subscribeOn(Schedulers.newSingle("handle-send-result-event-message"))
            .subscribe()
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    fun dispatchAfterCommit(paymentEventMessage: PaymentEventMessage){
        dispatch(paymentEventMessage)
    }

    fun dispatch(paymentEventMessage: PaymentEventMessage){
        //outbox 패턴으로 재시도하기 때문에 해당부분에서 재시도하지 않아도 됨
        sender.emitNext(createEventMessage(paymentEventMessage), Sinks.EmitFailureHandler.FAIL_FAST)
    }

    private fun createEventMessage(paymentEventMessage: PaymentEventMessage): Message<PaymentEventMessage> {
        return MessageBuilder.withPayload(paymentEventMessage)
                .setHeader(IntegrationMessageHeaderAccessor.CORRELATION_ID, paymentEventMessage.payload["orderId"])
                .setHeader(KafkaHeaders.PARTITION, paymentEventMessage.metadata["partitionKey"]
                        ?: 0)
                .build()
    }
}