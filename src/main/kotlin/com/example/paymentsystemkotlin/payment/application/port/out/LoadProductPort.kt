package com.example.paymentsystemkotlin.payment.application.port.out

import com.example.paymentsystemkotlin.payment.application.domain.Product
import reactor.core.publisher.Flux

interface LoadProductPort {

    fun getProducts(cartId: Long, productIds: List<Long>): Flux<Product>
}
