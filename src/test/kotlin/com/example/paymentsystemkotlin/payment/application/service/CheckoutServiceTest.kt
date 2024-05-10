package com.example.paymentsystemkotlin.payment.application.service

import com.example.paymentsystemkotlin.payment.application.port.`in`.CheckoutCommand
import com.example.paymentsystemkotlin.payment.application.port.`in`.CheckoutUsecase
import com.example.paymentsystemkotlin.payment.test.PaymentDatabaseHelper
import com.example.paymentsystemkotlin.payment.test.PaymentTestConfiguration
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.dao.DataIntegrityViolationException
import reactor.test.StepVerifier
import java.util.*

@SpringBootTest
@Import(PaymentTestConfiguration::class)
class CheckoutServiceTest(
        @Autowired private val checkoutUsecase: CheckoutUsecase,
        @Autowired private val paymentDatabaseHelper: PaymentDatabaseHelper
){

    @BeforeEach
    fun setup(){
        paymentDatabaseHelper.clean().block()
    }
    @Test
    fun `should save PaymentEvent and PaymentOrder successfully`(){
        val orderId = UUID.randomUUID().toString()
        val checkoutCommand = CheckoutCommand(
                cartId = 1,
                buyerId = 1,
                productIds = listOf(1,2,3),
                idempotencyKey = orderId
        )

        StepVerifier.create(checkoutUsecase.checkout(checkoutCommand))
                .expectNextMatches{
                    it.amount.toInt() == 60000 && it.orderId == orderId
                }
                .verifyComplete()

        val paymentEvent = paymentDatabaseHelper.getPayments(orderId)!!

        assertThat(paymentEvent.orderId).isEqualTo(orderId)
        assertThat(paymentEvent.totalAmount()).isEqualTo(60000)
        assertThat(paymentEvent.paymentOrders.size).isEqualTo(checkoutCommand.productIds.size)
        assertFalse(paymentEvent.isPaymentDone())
        assertTrue(paymentEvent.paymentOrders.all { !it.isLedgerUpdated() })
        assertTrue(paymentEvent.paymentOrders.all { !it.isWalletUpdated() })

    }

    @Test
    fun `should fail to save PaymentEvent and PaymentOrder when trying to save for the second time`(){
        val orderId = UUID.randomUUID().toString()
        val checkoutCommand = CheckoutCommand(
                cartId = 1,
                buyerId = 1,
                productIds = listOf(1,2,3),
                idempotencyKey = orderId
        )

        checkoutUsecase.checkout(checkoutCommand).block()

        assertThrows<DataIntegrityViolationException>{
            checkoutUsecase.checkout(checkoutCommand).block()
        }

    }
}