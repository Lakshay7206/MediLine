package com.example.mediline.User.ui.authentication

import android.util.Log.e
import com.google.firebase.FirebaseException

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class AuthViewModel : ViewModel() {

    private val auth = FirebaseAuth.getInstance()

    private val _uiState = MutableStateFlow(AuthState())
    val uiState: StateFlow<AuthState> = _uiState



    private var verificationId: String? = null

    fun sendOtp(phoneNumber: String) {
        _uiState.value = _uiState.value.copy(loading = true)

        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phoneNumber)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(_uiState.value.activity!!)
            .setCallbacks(object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                    signInWithCredential(credential)
                }

                override fun onVerificationFailed(p0: FirebaseException) {
                    _uiState.value = _uiState.value.copy(loading = false, error = p0.message)
                }

                override fun onCodeSent(verificationId: String, token: PhoneAuthProvider.ForceResendingToken) {
                    this@AuthViewModel.verificationId = verificationId
                    _uiState.value = _uiState.value.copy(
                        loading = false,
                        otpSent = true
                    )
                }
            }).build()

        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    fun verifyOtp(otp: String) {
        val credential = PhoneAuthProvider.getCredential(verificationId!!, otp)
        signInWithCredential(credential)
    }

    private fun signInWithCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                _uiState.value = _uiState.value.copy(loading = false, success = true)
            } else {
                _uiState.value = _uiState.value.copy(loading = false, error = task.exception?.message)
            }
        }
    }

    fun setActivity(activity: android.app.Activity) {
        _uiState.value = _uiState.value.copy(activity = activity)
    }
}


