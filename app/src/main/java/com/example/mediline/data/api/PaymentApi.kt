package com.example.mediline.data.api

import com.example.mediline.data.model.CreateOrderRequest
import com.example.mediline.data.model.CreateOrderResponse
import com.example.mediline.data.model.VerifyPaymentRequest
import com.example.mediline.data.model.VerifyPaymentResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface PaymentApi {
    @POST("payment/create-order")
    suspend fun createOrder(
        @Body request: CreateOrderRequest
    ): CreateOrderResponse

    @POST("payment/verify-payment")
    suspend fun verifyPayment(
        @Body request: VerifyPaymentRequest
    ): VerifyPaymentResponse
}