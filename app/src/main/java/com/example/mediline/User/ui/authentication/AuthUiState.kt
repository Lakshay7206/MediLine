package com.example.mediline.User.ui.authentication


sealed class AuthUiState {
    object Idle : AuthUiState() // Initial state
    object Loading : AuthUiState() // While doing any operation (sending/validating OTP, checking user, etc.)

    data class OtpSent(val verificationId: String) : AuthUiState() // OTP successfully sent

    data class UserExists(val uid: String) : AuthUiState() // Existing user found
    data class NewUser(val uid: String) : AuthUiState() // New user, needs profile setup
    object UserCreated : AuthUiState() // Successfully created new user

    data class Error(val message: String) : AuthUiState() // Any error state
}

data class AuthFormState(
    val phone: String = "",
    val uid: String = "",
    val otp: String = "",
    val verificationId: String = ""
)
