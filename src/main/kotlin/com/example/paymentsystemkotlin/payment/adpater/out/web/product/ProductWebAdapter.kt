package com.example.paymentsystemkotlin.payment.adpater.out.web.product

import com.example.paymentsystemkotlin.common.WebAdapter
import com.example.paymentsystemkotlin.payment.adpater.out.web.product.client.ProductClient
import com.example.paymentsystemkotlin.payment.application.domain.Product
import com.example.paymentsystemkotlin.payment.application.port.out.LoadProductPort
import reactor.core.publisher.Flux

@WebAdapter
class ProductWebAdapter(
        private val productClient: ProductClient
) :LoadProductPort{
    override fun getProducts(cartId: Long, productIds: List<Long>): Flux<Product> {
        return productClient.getProducts(cartId,productIds)
    }

}