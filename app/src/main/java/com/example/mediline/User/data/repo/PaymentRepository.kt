package com.example.mediline.User.data.repo

import com.example.mediline.User.data.model.PaymentRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.HttpURLConnection
import java.net.URL

class PaymentRepositoryImpl : PaymentRepository {
    override suspend fun createOrder(amount: Int, currency: String): Result<String> {
        return withContext(Dispatchers.IO) {
            try {
                // call your backend API -> /create-order
                val url = URL("http://10.0.2.2:3000/create-order")
                val connection = (url.openConnection() as HttpURLConnection).apply {
                    requestMethod = "POST"
                    doOutput = true
                    setRequestProperty("Content-Type", "application/json")
                }

                val body = """{"amount": $amount, "currency": "$currency"}"""
                connection.outputStream.use { os ->
                    os.write(body.toByteArray())
                }

                val response = connection.inputStream.bufferedReader().readText()
                Result.success(response) // should return orderId
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    override suspend fun verifyPayment(orderId: String, paymentId: String, signature: String): Result<Boolean> {
        return withContext(Dispatchers.IO) {
            try {
                // Backend endpoint (replace with your host)
                val url = URL("http://10.0.2.2:3000/verify-payment")

                val connection = (url.openConnection() as HttpURLConnection).apply {
                    requestMethod = "POST"
                    doOutput = true
                    setRequestProperty("Content-Type", "application/json")
                }

                // Send body
                val jsonBody = """
                {
                  "orderId": "$orderId",
                  "paymentId": "$paymentId",
                  "signature": "$signature"
                }
            """.trimIndent()

                connection.outputStream.use { os ->
                    os.write(jsonBody.toByteArray())
                }

                // Read response
                val response = connection.inputStream.bufferedReader().readText()

                // Suppose backend returns { "success": true }
                val success = response.contains("true")
                Result.success(success)

            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

}