package com.example.mediline.data.model

import android.app.Activity
import androidx.compose.ui.semantics.Role
import androidx.room.Entity
import androidx.room.PrimaryKey

data class User(
    val id: String = "",
   // val name: String = "",
    val phone: String = "",
    val createdAt: Long = System.currentTimeMillis(),
    val role: CreatorRole=CreatorRole.USER
)



data class UserEntity(
    val id: String = "",
    val phone: String = "",
    val createdAt: Long = System.currentTimeMillis(),
    val role: String = "USER" // Keep as String for Firebase
)
fun UserEntity.toDomain(): User {
    val safeRole = try {
        CreatorRole.valueOf(role)
    } catch (e: Exception) {
        CreatorRole.USER
    }
    return User(
        id = id,
        phone = phone,
        createdAt = createdAt,
        role = safeRole
    )
}



//sealed class OtpResult {
//    object AutoVerified : OtpResult()   // OTP auto-detected
//    data class CodeSent(val verificationId: String) : OtpResult()
//}