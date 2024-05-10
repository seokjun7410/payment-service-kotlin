package com.example.paymentsystemkotlin.payment.adpater.out.web.toss.executor

import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono

@Component
class TossPaymentExecutor(private val tossPaymentWebClient: WebClient, private val uri: String = "/v1/payments/confirm") {

    fun execute(paymentKey: String, orderId: String, amount: String): Mono<String> {
        return tossPaymentWebClient.post().uri(uri).header("Idempotency-Key", orderId).bodyValue("""
                        {
                          "paymentKey": "${paymentKey}",
                          "orderId": "${orderId}", 
                          "amount": ${amount}
                        }
                """.trimIndent()).retrieve().bodyToMono(String::class.java)
    }
}