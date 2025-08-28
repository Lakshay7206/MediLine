package com.example.mediline.data.model

import android.app.Activity

data class User(
    val id: String = "",
   // val name: String = "",
    val phone: String = "",
    val createdAt: Long = System.currentTimeMillis()
)

interface AuthRepository {
    suspend fun sendOtp(phone: String,activity: Activity): Result<String>
    suspend fun verifyOtp(verificationId: String, otp: String): Result<String> // returns uid
    suspend fun checkUserExists(uid: String): Result<Boolean>
    suspend fun createUser(user: User): Result<Unit>
}
//sealed class OtpResult {
//    object AutoVerified : OtpResult()   // OTP auto-detected
//    data class CodeSent(val verificationId: String) : OtpResult()
//}