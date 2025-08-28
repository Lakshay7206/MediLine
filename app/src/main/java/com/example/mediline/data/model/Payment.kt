package com.example.mediline.data.model

import com.google.gson.annotations.SerializedName

// CreateOrderRequest.kt
data class CreateOrderRequest(
    val amount: Int,
    val currency: String
)

// CreateOrderResponse.kt
data class CreateOrderResponse(
    @SerializedName("id") val orderId: String,
    val amount: Int,
    val currency: String,
    val status: String
)

// VerifyPaymentRequest.kt
data class VerifyPaymentRequest(
    val orderId: String,
    val paymentId: String,
    val signature: String
)

// VerifyPaymentResponse.kt
data class VerifyPaymentResponse(
    val success: Boolean
)


// Domain Layer
interface PaymentRepository {
    suspend fun createOrder(amount: Int, currency: String): Result<CreateOrderResponse>
    suspend fun verifyPayment(orderId: String, paymentId: String, signature: String): Result<Boolean>
}