package com.example.paymentsystemkotlin.payment.adpater.out.persistent.exception

import com.example.paymentsystemkotlin.payment.application.domain.PaymentStatus

class PaymentAlreadyProcessedException(
        val status: PaymentStatus,
        message: String) : RuntimeException(message) {

}
