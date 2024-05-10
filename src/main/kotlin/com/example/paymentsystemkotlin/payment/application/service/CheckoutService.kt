package com.example.paymentsystemkotlin.payment.application.service

import com.example.paymentsystemkotlin.common.UseCase
import com.example.paymentsystemkotlin.payment.application.domain.*
import com.example.paymentsystemkotlin.payment.application.port.`in`.CheckoutCommand
import com.example.paymentsystemkotlin.payment.application.port.`in`.CheckoutUsecase
import com.example.paymentsystemkotlin.payment.application.port.out.LoadProductPort
import com.example.paymentsystemkotlin.payment.application.port.out.SavePaymentPort
import reactor.core.publisher.Mono

@UseCase
class CheckoutService(
        private val loadProductPort: LoadProductPort,
        private val savePaymentPort: SavePaymentPort
) : CheckoutUsecase{
    override fun checkout(command: CheckoutCommand): Mono<CheckoutResult> {
        return loadProductPort.getProducts(command.cartId,command.productIds)
                .collectList()
                .map { createPaymentEvent(command,it) }
                .flatMap { savePaymentPort.save(it).thenReturn(it) }
                .map { CheckoutResult(amount = it.totalAmount(), orderId = it.orderId, orderName = it.orderName) }
    }

    private fun createPaymentEvent(command: CheckoutCommand, products:List<Product>): PaymentEvent {
        return PaymentEvent(
                buyerId = command.buyerId,
                orderId = command.idempotencyKey,
                orderName = products.joinToString { it.name },
                paymentOrders = products.map{
                    PaymentOrder(
                            sellerId = it.sellerId,
                            orderId = command.idempotencyKey,
                            productId = it.id,
                            amount =  it.amount,
                            paymentStatus = PaymentStatus.NOT_STARTED
                    )
                }
        )
    }
}