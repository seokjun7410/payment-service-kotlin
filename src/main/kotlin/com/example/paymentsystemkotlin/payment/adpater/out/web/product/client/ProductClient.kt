package com.example.paymentsystemkotlin.payment.adpater.out.web.product.client

import com.example.paymentsystemkotlin.payment.application.domain.Product
import reactor.core.publisher.Flux

interface ProductClient {
    fun getProducts(cartId: Long, productIds: List<Long>): Flux<Product>
}