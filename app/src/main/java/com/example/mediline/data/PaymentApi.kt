package com.example.mediline.data

// data/remote/PaymentApi.kt
import com.example.mediline.data.model.CreateOrderRequest
import com.example.mediline.data.model.CreateOrderResponse
import com.example.mediline.data.model.VerifyPaymentRequest
import com.example.mediline.data.model.VerifyPaymentResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface PaymentApi {
    @POST("create-order")
    suspend fun createOrder(
        @Body request: CreateOrderRequest
    ): CreateOrderResponse

    @POST("verify-payment")
    suspend fun verifyPayment(
        @Body request: VerifyPaymentRequest
    ): VerifyPaymentResponse
}
