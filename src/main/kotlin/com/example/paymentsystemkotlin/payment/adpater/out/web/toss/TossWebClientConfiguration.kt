package com.example.paymentsystemkotlin.payment.adpater.out.web.toss

import org.apache.hc.core5.http.HttpHeaders
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.client.reactive.ClientHttpConnector
import org.springframework.http.client.reactive.ReactorClientHttpConnector
import org.springframework.web.reactive.function.client.WebClient
import reactor.netty.http.client.HttpClient
import reactor.netty.resources.ConnectionProvider
import java.util.Base64

@Configuration
class TossWebClientConfiguration(
        @Value("\${PSP.toss.url}") private val baseUrl: String,
        @Value("\${PSP.toss.secretKey}") private val secretKey: String
) {
    @Bean
    fun tossPaymentWebClient(): WebClient {
        println(secretKey)
        val encodedSecretKey = Base64.getEncoder().encodeToString((secretKey).toByteArray());

        return WebClient.builder()
                .baseUrl(baseUrl)
                .defaultHeader(HttpHeaders.AUTHORIZATION,"Basic $encodedSecretKey")
                .defaultHeader(HttpHeaders.CONTENT_TYPE,"application/json")
                .clientConnector(reactorClientHttpConnector())
                .build()
    }

    private fun reactorClientHttpConnector(): ClientHttpConnector {
        val provider = ConnectionProvider.builder("toss-payment").build()
        return ReactorClientHttpConnector(HttpClient.create(provider))
    }
}