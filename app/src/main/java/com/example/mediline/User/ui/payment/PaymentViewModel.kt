package com.example.mediline.User.ui.payment

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mediline.User.dl.CreatePaymentOrderUseCase
import com.example.mediline.User.dl.VerifyPaymentUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PaymentViewModel @Inject constructor(
    private val createPaymentOrderUseCase: CreatePaymentOrderUseCase,
    private val verifyPaymentUseCase: VerifyPaymentUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(PaymentUiState())
    val uiState: StateFlow<PaymentUiState> = _uiState

    fun createOrder(amount: Int) {
        viewModelScope.launch {
            _uiState.value = PaymentUiState(loading = true)
            val result = createPaymentOrderUseCase(amount)
            _uiState.value = result.fold(
                onSuccess = { PaymentUiState(orderId = it)

                            },
                onFailure = { PaymentUiState(error = it.message)
                   }
            )
        }
    }
    fun handlePaymentSuccess(orderId: String, paymentId: String, signature: String) {
        viewModelScope.launch {
            _uiState.value = PaymentUiState(loading = true)
            val result = verifyPaymentUseCase(orderId, paymentId, signature)
            _uiState.value = result.fold(
                onSuccess = { verified ->
                    if (verified) {
                        PaymentUiState(verify = true)
                    } else {
                        PaymentUiState(error = "Verification failed ❌")
                    }
                },
                onFailure = { e ->
                    PaymentUiState(error = e.message ?: "Verification error")
                }
            )
        }
    }

    fun handlePaymentError(errorMsg: String) {
        _uiState.value = PaymentUiState(error = errorMsg)
    }
}