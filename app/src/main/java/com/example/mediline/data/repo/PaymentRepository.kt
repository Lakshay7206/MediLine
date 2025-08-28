package com.example.mediline.data.repo

import com.example.mediline.data.PaymentApi
import com.example.mediline.data.model.CreateOrderRequest
import com.example.mediline.data.model.CreateOrderResponse
import com.example.mediline.data.model.PaymentRepository
import com.example.mediline.data.model.VerifyPaymentRequest
import javax.inject.Inject


class PaymentRepositoryImpl @Inject constructor(
    private val api: PaymentApi
) : PaymentRepository {

    override suspend fun createOrder(amount: Int, currency: String): Result<CreateOrderResponse> {
        return try {
            val response = api.createOrder(CreateOrderRequest(amount, currency))
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun verifyPayment(orderId: String, paymentId: String, signature: String): Result<Boolean> {
        return try {
            val response = api.verifyPayment(VerifyPaymentRequest(orderId, paymentId, signature))
            Result.success(response.success)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}


//class PaymentRepositoryImpl : PaymentRepository {
//    override suspend fun createOrder(amount: Int, currency: String): Result<String> {
//        return withContext(Dispatchers.IO) {
//            try {
//                // call your backend API -> /create-order
//                val url = URL("https://myapp.ngrok-free.app/create-order")
//                val connection = (url.openConnection() as HttpURLConnection).apply {
//                    requestMethod = "POST"
//                    doOutput = true
//                    setRequestProperty("Content-Type", "application/json")
//                }
//
//                val body = """{"amount": $amount, "currency": "$currency"}"""
//                connection.outputStream.use { os ->
//                    os.write(body.toByteArray())
//                }
//
//                val response = connection.inputStream.bufferedReader().readText()
//                Result.success(response) // should return orderId
//            } catch (e: Exception) {
//                Result.failure(e)
//            }
//        }
//    }
//
//    override suspend fun verifyPayment(orderId: String, paymentId: String, signature: String): Result<Boolean> {
//        return withContext(Dispatchers.IO) {
//            try {
//                // Backend endpoint (replace with your host)
//                val url = URL("https://myapp.ngrok-free.app/verify-payment")
//
//                val connection = (url.openConnection() as HttpURLConnection).apply {
//                    requestMethod = "POST"
//                    doOutput = true
//                    setRequestProperty("Content-Type", "application/json")
//                }
//
//                // Send body
//                val jsonBody = """
//                {
//                  "orderId": "$orderId",
//                  "paymentId": "$paymentId",
//                  "signature": "$signature"
//                }
//            """.trimIndent()
//
//                connection.outputStream.use { os ->
//                    os.write(jsonBody.toByteArray())
//                }
//
//                // Read response
//                val response = connection.inputStream.bufferedReader().readText()
//
//                // Suppose backend returns { "success": true }
//                val success = response.contains("true")
//                Result.success(success)
//
//            } catch (e: Exception) {
//                Result.failure(e)
//            }
//        }
//    }
//
//}