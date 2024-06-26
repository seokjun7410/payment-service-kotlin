package com.example.paymentsystemkotlin.common

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.registerKotlinModule

val objectMapper = ObjectMapper()
        .registerKotlinModule()//jackson 은 java객체를 다루도록 설젇되어있기 때문에 KotlinModule 등록
        .registerModules(Jdk8Module, JavaTimeModule) // 날짜와 시간 직렬화에 필요
        .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS,false) //날짜 직렬화시 타임스탬프 대신 날짜형식으로 변환