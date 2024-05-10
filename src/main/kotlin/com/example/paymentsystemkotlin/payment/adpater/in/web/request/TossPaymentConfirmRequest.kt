package com.example.paymentsystemkotlin.payment.adpater.`in`.web.request

data class TossPaymentConfirmRequest(
        val paymentKey: String,
        val orderId: String,
        val amount: String
)
