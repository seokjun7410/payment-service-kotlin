package com.example.paymentsystemkotlin.payment.adpater.out.web.product.client

import com.example.paymentsystemkotlin.payment.application.domain.Product
import org.springframework.stereotype.Component
import reactor.core.publisher.Flux
import java.math.BigDecimal

/**
 * 예제 간편화를 위한 MockClient
 */
@Component
class MockProductClient: ProductClient {
    override fun getProducts(cartId: Long, productIds: List<Long>): Flux<Product> {
       return Flux.fromIterable(
               productIds.map {
                   Product(
                           id = it,
                           amount = BigDecimal(it * 10000),
                           quantity = 2,
                           name = "test_product_$it",
                           sellerId = 1
                   )
               }
       )
    }

}