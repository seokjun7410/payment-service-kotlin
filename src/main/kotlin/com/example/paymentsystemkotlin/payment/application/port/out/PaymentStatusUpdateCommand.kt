package com.example.paymentsystemkotlin.payment.application.port.out

import com.example.paymentsystemkotlin.payment.application.domain.PaymentExtraDetails
import com.example.paymentsystemkotlin.payment.application.domain.PaymentFailure
import com.example.paymentsystemkotlin.payment.application.domain.PaymentStatus

data class PaymentStatusUpdateCommand(
        val paymentKey: String,
        val orderId: String,
        val status: PaymentStatus,
        val extraDetails: PaymentExtraDetails? = null,
        val failure: PaymentFailure? = null
){
    init{
        require(status == PaymentStatus.SUCCESS || status == PaymentStatus.FAILURE || status == PaymentStatus.UNKNOWN){
            "결제 상태 (status: $status)는 올바르지 않은 결제 상태입니다."
        }

        if(status == PaymentStatus.SUCCESS){
            requireNotNull(extraDetails) { "PaymentStatus 값이 SUCCESS 라면 PaymentExtraDetails null 이 되면 안됩니다."}
        }else if(status == PaymentStatus.FAILURE){
            requireNotNull(failure) { "PaymentStatus 값이 FAILURE 라면 PaymentFailure null 이 되면 안됩니다."}
        }
    }
}
