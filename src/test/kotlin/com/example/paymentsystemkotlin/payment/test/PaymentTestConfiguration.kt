package com.example.paymentsystemkotlin.payment.test

import org.springframework.context.annotation.Bean
import org.springframework.r2dbc.core.DatabaseClient
import org.springframework.transaction.reactive.TransactionalOperator

class PaymentTestConfiguration {

    @Bean
    fun paymentDatabaseHelper(databaseClient: DatabaseClient, transactionalOperator: TransactionalOperator): PaymentDatabaseHelper{
        return R2DBCPaymentDatabaseHelper(databaseClient, transactionalOperator)
    }
}