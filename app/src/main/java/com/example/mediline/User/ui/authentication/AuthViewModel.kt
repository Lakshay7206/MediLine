package com.example.mediline.User.ui.authentication

import android.app.Activity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mediline.User.dl.CheckUserExistsUseCase
import com.example.mediline.User.dl.CreateUserUseCase
import com.example.mediline.User.dl.SendOtpUseCase
import com.example.mediline.User.data.model.User
import com.example.mediline.User.dl.VerifyOtpUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val sendOtpUseCase: SendOtpUseCase,
    private val verifyOtpUseCase: VerifyOtpUseCase,
    private val checkUserExistsUseCase: CheckUserExistsUseCase,
    private val createUserUseCase: CreateUserUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<AuthUiState>(AuthUiState.Idle)
    val uiState: StateFlow<AuthUiState> = _uiState

    private val _formState= MutableStateFlow(AuthFormState())
    val formState: StateFlow<AuthFormState> = _formState


    fun sendOtp(phone: String,activity: Activity) {
        viewModelScope.launch {
            _uiState.value = AuthUiState.Loading
            val result = sendOtpUseCase(phone, activity)
            _uiState.value = result.fold(
                onSuccess = { verificationId ->
                    _formState.value=formState.value.copy(verificationId = verificationId)
                    AuthUiState.OtpSent(verificationId)
                            },
                onFailure = { error -> AuthUiState.Error(error.message ?: "Unknown error") }
            )
        }
    }

    fun verifyOtp(verificationId: String, otp: String) {
        viewModelScope.launch {
            _uiState.value = AuthUiState.Loading
            val result = verifyOtpUseCase(verificationId, otp)
            result.fold(
                onSuccess = { uid ->
                    _formState.value=formState.value.copy(uid = uid)
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


    fun updatePhone(phone:String){

            _formState.value=formState.value.copy(phone = phone)

    }

    fun updateOtp(otp:String){

            _formState.value=formState.value.copy(otp = otp)


    }

}











