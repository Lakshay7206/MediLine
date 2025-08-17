package com.example.mediline.User.ui.authentication

import android.app.Activity
import android.telephony.SignalStrengthUpdateRequest
import android.util.Log.e
import com.google.firebase.FirebaseException

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.Navigation
import com.example.mediline.User.Screen
import com.example.mediline.User.UiEvent
import com.example.mediline.User.data.repo.AuthRepository
import com.example.mediline.User.data.repo.UserRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.rpc.context.AttributeContext
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(private val userRepo: UserRepository,private val authRepo: AuthRepository) : ViewModel() {



    private val _uiState = MutableStateFlow(AuthState())
    val uiState: StateFlow<AuthState> = _uiState
    private val _uiEvent= MutableSharedFlow<UiEvent>()
    val uiEvent= _uiEvent.asSharedFlow()

    fun setActivity(activity: android.app.Activity) {
        _uiState.value = _uiState.value.copy(activity = activity)
    }

    fun sendOtp(phone: String) {
        viewModelScope.launch {
            _uiState.value = AuthState(loading = true)

            val result = userRepo.sendOtp(phone,_uiState.value.activity!!)

            result.onSuccess { verificationId ->
                _uiState.value = AuthState(verificationId = verificationId)
            }.onFailure { e ->
                _uiState.value = AuthState(error = e.message)
            }
        }
    }

    fun verifyOtp(verificationId: String, otp: String, name: String) {
        viewModelScope.launch {
            _uiState.value = AuthState(loading = true)

            val result = userRepo.verifyOtp(verificationId, otp, name)

            result.onSuccess {
                _uiState.value = AuthState(success = true)
            }.onFailure { e ->
                _uiState.value = AuthState(error = e.message)
            }
        }
    }











    fun NavigateSignUp(){
        viewModelScope.launch {
            _uiEvent.emit(UiEvent.Navigation(Screen.Signup.route))
        }
    }

}


