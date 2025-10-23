package com.example.mediline.data.model

import com.google.gson.annotations.SerializedName


data class CreateOrderRequest(
    val amount: Int,
    val currency: String
)


data class CreateOrderResponse(
    @SerializedName("id") val orderId: String,
    val amount: Int,
    val currency: String,
    val status: String
)


data class VerifyPaymentRequest(
    val orderId: String,
    val paymentId: String,
    val signature: String,
   // val departmentId:String
)


data class VerifyPaymentResponse(
    val success: Boolean
)



interface PaymentRepository {
    suspend fun createOrder(amount: Int, currency: String): Result<CreateOrderResponse>
    suspend fun verifyPayment(orderId: String, paymentId: String, signature: String): Result<Boolean>
}