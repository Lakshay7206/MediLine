package com.example.mediline.User.ui.authentication

import android.app.Activity
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mediline.dl.CheckUserExistsUseCase
import com.example.mediline.dl.CreateUserUseCase
import com.example.mediline.dl.SendOtpUseCase
import com.example.mediline.data.model.User
import com.example.mediline.dl.ResendOtpUseCase
import com.example.mediline.dl.VerifyOtpUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

//@HiltViewModel
//class AuthViewModel @Inject constructor(
//    private val sendOtpUseCase: SendOtpUseCase,
//    private val verifyOtpUseCase: VerifyOtpUseCase,
//    private val checkUserExistsUseCase: CheckUserExistsUseCase,
//    private val createUserUseCase: CreateUserUseCase
//) : ViewModel() {
//
//    private val _uiState = MutableStateFlow<AuthUiState>(AuthUiState.Idle)
//    val uiState: StateFlow<AuthUiState> = _uiState
//
//    private val _formState= MutableStateFlow(AuthFormState())
//    val formState: StateFlow<AuthFormState> = _formState
//
//
//    fun sendOtp(phone: String,activity: Activity) {
//        viewModelScope.launch {
//            _uiState.value = AuthUiState.Loading
//            val result = sendOtpUseCase(phone, activity)
//            _uiState.value = result.fold(
//                onSuccess = { verificationId ->
//                    _formState.value=formState.value.copy(verificationId = verificationId)
//                    AuthUiState.OtpSent(verificationId)
//                            },
//                onFailure = { error -> AuthUiState.Error(error.message ?: "Unknown error") }
//            )
//        }
//    }
//
//    fun verifyOtp(verificationId: String, otp: String) {
//        viewModelScope.launch {
//            _uiState.value = AuthUiState.Loading
//            val result = verifyOtpUseCase(verificationId, otp)
//            result.fold(
//                onSuccess = { uid ->
//                    _formState.value=formState.value.copy(uid = uid)
//                    checkUser(uid)
//                },
//                onFailure = { error ->
//                    _uiState.value = AuthUiState.Error(error.message ?: "Invalid OTP")
//                }
//            )
//        }
//    }
//
//    private fun checkUser(uid: String) {
//        viewModelScope.launch {
//            val result = checkUserExistsUseCase(uid)
//            result.fold(
//                onSuccess = { exists ->
//                    if (exists) {
//                        _uiState.value = AuthUiState.UserExists(uid)
//                    } else {
//                        _uiState.value = AuthUiState.NewUser(uid)
//                    }
//                },
//                onFailure = { error ->
//                    _uiState.value = AuthUiState.Error(error.message ?: "Failed to check user")
//                }
//            )
//        }
//    }
//
//    fun createUser(user: User) {
//        viewModelScope.launch {
//            val result = createUserUseCase(user)
//            result.fold(
//                onSuccess = {
//                    _uiState.value = AuthUiState.UserCreated
//                },
//                onFailure = { error ->
//                    _uiState.value = AuthUiState.Error(error.message ?: "Failed to create user")
//                }
//            )
//        }
//    }
//
//
//    fun updatePhone(phone:String){
//
//            _formState.value=formState.value.copy(phone = "+91${phone}")
//        Log.d("Loginl", "${_formState.value}")
//
//    }
//
//    fun updateOtp(otp:String){
//
//            _formState.value=formState.value.copy(otp = otp)
//
//
//    }
//
//}
//
//
//


@HiltViewModel
class AuthViewModel @Inject constructor(
    private val sendOtpUseCase: SendOtpUseCase,
    private val verifyOtpUseCase: VerifyOtpUseCase,
    private val checkUserExistsUseCase: CheckUserExistsUseCase,
    private val createUserUseCase: CreateUserUseCase,
    private val resendOtpUseCase: ResendOtpUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<AuthUiState>(AuthUiState.Idle)
    val uiState: StateFlow<AuthUiState> = _uiState

    private val _formState = MutableStateFlow(AuthFormState())
    val formState: StateFlow<AuthFormState> = _formState

    // Track resend attempts and cooldown
    private var cooldownJob: Job? = null

    fun sendOtp(phone: String, activity: Activity) {
        viewModelScope.launch {
            if (_formState.value.resendAttempts >= 3) {
                _uiState.value = AuthUiState.Error("Max resend attempts reached")
                return@launch
            }
            if (_formState.value.cooldownSeconds > 0) {
                _uiState.value = AuthUiState.Error("Wait for cooldown to finish")
                return@launch
            }

            _uiState.value = AuthUiState.Loading
            val result = sendOtpUseCase(phone, activity)
            _uiState.value = result.fold(
                onSuccess = { otpData ->
                    _formState.value = formState.value.copy(
                        phone = phone,
                        verificationId = otpData.verificationId,
                        resendToken = otpData.resendToken,
                        resendAttempts = formState.value.resendAttempts + 1
                    )
                    startCooldown()
                    AuthUiState.OtpSent(otpData.verificationId)
                },
                onFailure = { error ->
                    AuthUiState.Error(error.message ?: "Unknown error")
                }
            )
        }
    }
    fun resendOtp(activity: Activity) {
        val phone = formState.value.phone
        val token = formState.value.resendToken
        if (token == null) {
            _uiState.value = AuthUiState.Error("Cannot resend OTP. Missing phone or token.")
            return
        }

        viewModelScope.launch {
            if (_formState.value.resendAttempts >= 3) {
                _uiState.value = AuthUiState.Error("Max resend attempts reached")
                return@launch
            }
            if (_formState.value.cooldownSeconds > 0) {
                _uiState.value = AuthUiState.Error("Wait for cooldown to finish")
                return@launch
            }

            _uiState.value = AuthUiState.Loading
            val result = resendOtpUseCase(phone, activity, token)
            _uiState.value = result.fold(
                onSuccess = { otpData ->
                    _formState.value = formState.value.copy(
                        verificationId = otpData.verificationId,
                        resendToken = otpData.resendToken,
                        resendAttempts = formState.value.resendAttempts + 1
                    )
                    startCooldown()
                    AuthUiState.OtpSent(otpData.verificationId)
                },
                onFailure = { error ->
                    AuthUiState.Error(error.message ?: "Failed to resend OTP")
                }
            )
        }
    }

    fun verifyOtp(verificationId: String, otp: String) {
        viewModelScope.launch {
            _uiState.value = AuthUiState.Loading
            val result = verifyOtpUseCase(verificationId, otp)
            result.fold(
                onSuccess = { uid ->
                    _formState.value = formState.value.copy(uid = uid)
                    checkUser(uid)
                },
                onFailure = { error ->
                    _uiState.value = AuthUiState.Error(error.message ?: "Invalid OTP")
                }
            )
        }
    }

    private fun checkUser(uid: String) {
        viewModelScope.launch {
            val result = checkUserExistsUseCase(uid)
            result.fold(
                onSuccess = { exists ->
                    if (exists) {
                        _uiState.value = AuthUiState.UserExists(uid)
                    } else {
                        _uiState.value = AuthUiState.NewUser(uid)
                    }
                },
                onFailure = { error ->
                    _uiState.value = AuthUiState.Error(error.message ?: "Failed to check user")
                }
            )
        }
    }

    fun createUser(user: User) {
        viewModelScope.launch {
            val result = createUserUseCase(user)
            result.fold(
                onSuccess = {
                    _uiState.value = AuthUiState.UserCreated
                },
                onFailure = { error ->
                    _uiState.value = AuthUiState.Error(error.message ?: "Failed to create user")
                }
            )
        }
    }

    fun updatePhone(phone: String) {
        _formState.value = formState.value.copy(phone = phone)
        Log.d("Loginl", "${_formState.value}")
    }

    fun updateOtp(otp: String) {
        _formState.value = formState.value.copy(otp = otp)
    }

    // 🔹 Start cooldown timer (e.g., 30 seconds)
    private fun startCooldown() {
        cooldownJob?.cancel()
        _formState.value.cooldownSeconds = 30
        cooldownJob = viewModelScope.launch {
            while (_formState.value.cooldownSeconds > 0) {
                delay(1000)
                _formState.value.cooldownSeconds = _formState.value.cooldownSeconds - 1
            }
        }
    }
}





