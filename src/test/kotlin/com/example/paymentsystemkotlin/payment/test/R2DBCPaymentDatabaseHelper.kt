package com.example.paymentsystemkotlin.payment.test

import com.example.paymentsystemkotlin.payment.application.domain.*
import org.springframework.r2dbc.core.DatabaseClient
import org.springframework.transaction.reactive.TransactionalOperator
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono
import java.math.BigDecimal
import java.time.LocalDateTime
import java.time.ZonedDateTime

class R2DBCPaymentDatabaseHelper(
        private val databaseClient: DatabaseClient,
        private val transactionalOperator: TransactionalOperator
): PaymentDatabaseHelper {
    override fun getPayments(orderId: String): PaymentEvent? {
        return databaseClient.sql(SELECT_PAYMENT_QUERY)
                .bind("orderId",orderId)
                .fetch()
                .all()
                .groupBy { it["payment_event_id"] as Long }
                .flatMap {groupedFlux->
                    groupedFlux.collectList().map { result->
                        PaymentEvent(
                                id = groupedFlux.key(),
                                orderId =  result.first()["order_id"] as String,
                                orderName = result.first()["order_name"] as String,
                                buyerId =  result.first()["buyer_id"] as Long,
                                paymentKey = result.first()["payment_key"] as String?,
                                paymentType = if(result.first()["type"] != null) PaymentType.get(result.first()["type"] as String) else null,
                                paymentMethod = if(result.first()["method"] != null) PaymentMethod.get(result.first()["method"] as String) else null,
                                approvedAt = if(result.first()["approved_at"] != null) (result.first()["approved_at"] as ZonedDateTime).toLocalDateTime() else null,
                                isPaymentDone = if((result.first()["is_payment_done"] as Byte).toInt() == 1) true else false,
                                paymentOrders = result.map { result ->
                                    PaymentOrder(
                                            id = result["id"] as Long,
                                            paymentEventId = groupedFlux.key(),
                                            sellerId = result["seller_id"] as Long,
                                            orderId = result["order_id"] as String,
                                            productId = result["product_id"] as Long,
                                            amount = result["amount"] as BigDecimal,
                                            paymentStatus = PaymentStatus.get(result["payment_order_status"] as String),
                                            isLedgerUpdated = ((result["ledger_updated"]) as Byte).toInt() == 1,
                                            isWalletUpdated = ((result["wallet_updated"]) as Byte).toInt() == 1
                                    )
                                }
                        )
                    }
                }.toMono().block()
    }

    override fun clean():Mono<Void> {
        return deletePaymentOrderHistory()
                .flatMap { deletePaymentOrders() }
                .flatMap { deletePaymentEvents() }
                .`as` ( transactionalOperator::transactional )
                .then()

    }

    private fun deletePaymentEvents():Mono<Long> {
        return databaseClient.sql(DELETE_PAYMENT_EVENT_QUERY)
            .fetch()
            .rowsUpdated()
    }

    private fun deletePaymentOrders(): Mono<Long> {
        return databaseClient.sql(DELETE_PAYMENT_ORDER_QUERY)
                .fetch()
                .rowsUpdated()
    }

    private fun deletePaymentOrderHistory(): Mono<Long> {
        return databaseClient.sql(DELETE_PAYMENT_ORDER_HISTORY_QUERY)
                .fetch()
                .rowsUpdated()
    }


    companion object{
        val SELECT_PAYMENT_QUERY = """
            SELECT * FROM payment_events pe
            INNER JOIN payment_orders po ON pe.order_id = po.order_id
            WHERE pe.order_id = :orderId
        """.trimIndent()

        val DELETE_PAYMENT_EVENT_QUERY = """
            DELETE FROM payment_events
        """.trimIndent()

        val DELETE_PAYMENT_ORDER_QUERY = """
            DELETE FROM payment_orders
        """.trimIndent()

        val DELETE_PAYMENT_ORDER_HISTORY_QUERY = """
            DELETE FROM payment_order_history
        """.trimIndent()
    }
}