package com.example.paymentsystemkotlin.payment.adpater.out.stream.util

import org.springframework.stereotype.Component
import kotlin.math.abs

@Component
class PartitionKeyUtil{
    val PARTITION_KEY_COUNT = 6 // 카프파 Payment 토픽 파티션 값


    fun createPartitionKey(number: Int): Int{
        return abs(number) % PARTITION_KEY_COUNT
    }

}