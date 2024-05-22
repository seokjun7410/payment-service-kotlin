package com.example.paymentsystemkotlin.payment.application.domain

enum class PaymentMethod(val description: String ) {
    EASY_PAY("간편결제");


    companion object {
        fun get(type: String): PaymentMethod {
            return PaymentMethod.entries.find { it.description == type } ?: error("PaymentMethod (type: $type) 은 올바르지 않은 결제 수단입니다.")
        }
    }
}
