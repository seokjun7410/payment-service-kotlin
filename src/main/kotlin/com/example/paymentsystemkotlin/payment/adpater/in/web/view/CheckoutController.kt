package com.example.paymentsystemkotlin.payment.adpater.`in`.web.view

import com.example.paymentsystemkotlin.common.IdempotencyCreator
import com.example.paymentsystemkotlin.common.WebAdapter
import com.example.paymentsystemkotlin.payment.adpater.`in`.web.request.CheckoutMockRequest
import com.example.paymentsystemkotlin.payment.application.port.`in`.CheckoutCommand
import com.example.paymentsystemkotlin.payment.application.port.`in`.CheckoutUsecase
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import reactor.core.publisher.Mono

@Controller
@WebAdapter
class CheckoutController (
        private val checkoutUsecase: CheckoutUsecase
){

    @GetMapping("/")
    fun checkoutPage(request: CheckoutMockRequest, model: Model): Mono<String> {
        val command = CheckoutCommand(
                cartId = request.cartId,
                buyerId = request.buyerId,
                productIds = request.productIds,
                idempotencyKey = IdempotencyCreator.create(request)
        )

        return checkoutUsecase.checkout(command)
                .map {
                    model.addAttribute("orderId", it.orderId)
                    model.addAttribute("orderName", it.orderName)
                    model.addAttribute("amount", it.amount)
                    "checkout"
                }

    }

}