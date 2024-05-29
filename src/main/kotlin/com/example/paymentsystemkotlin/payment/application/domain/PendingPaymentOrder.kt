package com.example.paymentsystemkotlin.payment.application.domain

data class PendingPaymentOrder (
        val paymentOrderId: Long,
        val status: PaymentStatus,
        val amount: Long,
        val failedCount: Byte,
        val threshold: Byte
)
