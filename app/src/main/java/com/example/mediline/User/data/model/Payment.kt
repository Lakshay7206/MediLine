package com.example.mediline.User.data.model

interface PaymentRepository {
    suspend fun createOrder(amount: Int, currency: String = "INR"): Result<String>
    suspend fun verifyPayment(orderId: String, paymentId: String, signature: String): Result<Boolean>
}
