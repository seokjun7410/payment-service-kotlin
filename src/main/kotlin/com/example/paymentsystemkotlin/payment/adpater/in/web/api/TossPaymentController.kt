package com.example.paymentsystemkotlin.payment.adpater.`in`.web.api

import com.example.paymentsystemkotlin.common.WebAdapter
import com.example.paymentsystemkotlin.payment.adpater.`in`.web.request.TossPaymentConfirmRequest
import com.example.paymentsystemkotlin.payment.adpater.`in`.web.response.ApiResponse
import com.example.paymentsystemkotlin.payment.adpater.out.web.toss.executor.TossPaymentExecutor
import com.example.paymentsystemkotlin.payment.application.domain.PaymentConfirmationResult
import com.example.paymentsystemkotlin.payment.application.port.`in`.PaymentConfirmCommand
import com.example.paymentsystemkotlin.payment.application.port.`in`.PaymentConfirmUsecase
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@WebAdapter
@RequestMapping("/v1/toss")
@RestController
class TossPaymentController(
        private val paymentConfirmUsecase: PaymentConfirmUsecase
) {

    @PostMapping("/confirm")
    fun confirm(@RequestBody request: TossPaymentConfirmRequest): Mono<ResponseEntity<ApiResponse<PaymentConfirmationResult>>> {
        val command = PaymentConfirmCommand(
                paymentKey = request.paymentKey,
                orderId = request.orderId,
                amount = request.amount.toLong()
        )

        return paymentConfirmUsecase.confirm(command)
                .map { ResponseEntity.ok().body(ApiResponse.with(HttpStatus.OK,"",it)) }
    }

}