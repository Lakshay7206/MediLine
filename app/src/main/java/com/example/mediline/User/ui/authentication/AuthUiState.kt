package com.example.mediline.User.ui.authentication

class LoginUiState {
    val phone: String = ""

}

data class AuthState(
    val loading: Boolean = false,
    val otpSent: Boolean = false,
    val success: Boolean = false,
    val error: String? = null,
    val activity: android.app.Activity? = null,
    val verificationId: String? = null,
    val isNewUser: Boolean? = null

)
