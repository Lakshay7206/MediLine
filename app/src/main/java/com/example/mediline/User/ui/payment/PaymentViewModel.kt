package com.example.mediline.User.ui.payment

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mediline.User.dl.CreateOrderUseCase

import com.example.mediline.User.dl.VerifyPaymentUseCase
import com.example.mediline.User.ui.authentication.LoginScreen
import com.razorpay.PaymentData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.Dispatcher
import javax.inject.Inject

enum class PaymentGateway { RAZORPAY, PAYTM, STRIPE }


@HiltViewModel
class PaymentViewModel @Inject constructor(
    private val createOrderUseCase: CreateOrderUseCase,
    private val verifyPaymentUseCase: VerifyPaymentUseCase
) : ViewModel() {

    private val _paymentState = MutableStateFlow<PaymentState>(PaymentState.Idle)
    val paymentState: StateFlow<PaymentState> = _paymentState

    private val _selectedGateway = MutableStateFlow<String?>(null)
    val selectedGateway: StateFlow<String?> = _selectedGateway

    fun onGatewaySelected(gateway: String) {
        _selectedGateway.value = gateway
    }

    fun startPayment(amount: Int, currency: String) {
        viewModelScope.launch {
            _paymentState.value = PaymentState.Loading
            createOrderUseCase(amount, currency).fold(
                onSuccess = { order -> _paymentState.value = PaymentState.OrderCreated(order) },
                onFailure = { _paymentState.value = PaymentState.Error(it.message ?: "Error") }
            )
        }
    }

    fun verifyPayment(orderId: String, paymentId: String, signature: String) {
        viewModelScope.launch {
            verifyPaymentUseCase(orderId, paymentId, signature).fold(
                onSuccess = { success ->
                    _paymentState.value =
                        if (success) PaymentState.Success else PaymentState.Error("Verification failed")
                },
                onFailure = { _paymentState.value = PaymentState.Error(it.message ?: "Error") }
            )
        }
    }

    fun markPaymentFailed(message: String) {
        _paymentState.value = PaymentState.Error(message)
    }
}




//@HiltViewModel
//class PaymentViewModel @Inject constructor(
//    private val createOrderUseCase: CreateOrderUseCase,
//    private val verifyPaymentUseCase: VerifyPaymentUseCase
//) : ViewModel() {
//
//
//    private val _selectedGateway = MutableStateFlow<String?>(null)
//    val selectedGateway: StateFlow<String?> = _selectedGateway
//
//    fun onGatewaySelected(gateway: String) {
//        _selectedGateway.value = gateway
//    }
//    private val _paymentState = MutableStateFlow<PaymentState>(PaymentState.Idle)
//    val paymentState: StateFlow<PaymentState> = _paymentState
//
//
//
////    val lastOrderId: String?
////        get() = (_paymentState.value as? PaymentState.OrderCreated)?.order?.orderId
//
//    fun markPaymentFailed(msg: String) {
//        _paymentState.value = PaymentState.Error(msg)
//    }
//
//    fun startPayment(amount: Int, currency: String) {
//
//        viewModelScope.launch {
//            _paymentState.value = PaymentState.Loading
//            try {
//                val result = createOrderUseCase(amount, currency) // pass gateway
//                result.fold(
//                    onSuccess = { orderId ->
//                        _paymentState.value = PaymentState.OrderCreated(orderId)
//                    },
//                    onFailure = { _paymentState.value = PaymentState.Error(it.message ?: "Error") }
//                )
//            } catch (e: Exception) {
//                _paymentState.value = PaymentState.Error(e.message ?: "Unexpected error")
//            }
//        }
//    }
//
//    fun verifyPayment(orderId: String, paymentId: String, signature: String){
//        viewModelScope.launch {
//            try {
//                Log.d("PaymentViewModel", "Verifying payment ")
//                val result = verifyPaymentUseCase(orderId, paymentId, signature)
//                result.fold(
//                    onSuccess = { success ->
//                        Log.e("Razorpay", "Backend verification success: $success")
//                        _paymentState.value =
//                            if (success) PaymentState.Success else PaymentState.Error("Verification failed")
//                    },
//                    onFailure = { _paymentState.value = PaymentState.Error(it.message ?: "Error") }
//                )
//            } catch (e: Exception) {
//                _paymentState.value = PaymentState.Error(e.message ?: "Unexpected error")
//            }
//        }
//    }
//}

