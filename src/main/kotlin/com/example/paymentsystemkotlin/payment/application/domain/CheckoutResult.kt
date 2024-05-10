package com.example.paymentsystemkotlin.payment.application.domain

data class CheckoutResult (
        val amount: Long,
        val orderId: String,
        val orderName: String
)