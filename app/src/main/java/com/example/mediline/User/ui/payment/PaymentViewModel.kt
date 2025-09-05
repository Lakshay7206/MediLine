    package com.example.mediline.User.ui.payment

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import androidx.savedstate.savedState
import com.example.mediline.User.ui.authentication.LoginScreen
import com.example.mediline.data.repo.PaymentDataHolder
import com.example.mediline.dl.CreateOrderUseCase
import com.example.mediline.dl.UserUpdatePaymentStatusUseCase

import com.example.mediline.dl.VerifyPaymentUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

enum class PaymentGateway { RAZORPAY, PAYTM, STRIPE }


@HiltViewModel
class PaymentViewModel @Inject constructor(
    private val createOrderUseCase: CreateOrderUseCase,
    private val verifyPaymentUseCase: VerifyPaymentUseCase,
    private val userUpdatePaymentStatusUseCase: UserUpdatePaymentStatusUseCase,

) : ViewModel() {

    private val _paymentState = MutableStateFlow<PaymentState>(PaymentState.Idle)
    val paymentState: StateFlow<PaymentState> = _paymentState
    private val _eventFlow = MutableSharedFlow<PaymentEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private val _selectedGateway = MutableStateFlow<String?>(null)
    val selectedGateway: StateFlow<String?> = _selectedGateway


//    private val _formId = MutableStateFlow(savedStateHandle.get<String>("formId") ?: "")
//    val formId: StateFlow<String> = _formId
//
//    fun updateFormId(id: String) {
//        _formId.value = id
//        savedStateHandle["formId"] = id
//        Log.d("Payment","updated ${_formId.value}")
//    // persist across recreation
//    }

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
            val currentFormId = PaymentDataHolder.formId
            if (currentFormId.isNullOrBlank()) {
                Log.e("Payment", "Form ID is empty! Cannot verify payment.")
                _paymentState.value = PaymentState.Error("Form ID missing")
                return@launch
            }

            try {
                Log.d("Payment", "Starting verification for formId=$currentFormId orderId=$orderId")
                _paymentState.value = PaymentState.Loading

                // Call your verification use-case on IO
                val verifyResult = withContext(Dispatchers.IO) {
                    runCatching {
                        verifyPaymentUseCase(orderId, paymentId, signature)
                    }.getOrThrow()  // will throw if the use case threw
                }

                // If your verifyPaymentUseCase returns Result<Boolean>, handle accordingly:
                verifyResult.fold( // if it's Result<Boolean>
                    onSuccess = { success ->
                        if (!success) {
                            Log.e("Payment", "Remote verification returned false")
                            _paymentState.value = PaymentState.Error("Verification failed")
                            return@launch
                        }

                        // Update Firestore / backend status
                        val updateResult = withContext(Dispatchers.IO) {
                            runCatching {
                                userUpdatePaymentStatusUseCase(currentFormId, "PAID")
                            }.getOrThrow()
                        }

                        // If userUpdatePaymentStatusUseCase returns Result<Unit> or Boolean:
                        // try to detect both cases
                        when (updateResult) {
                            is Result<*> -> {
                                updateResult.fold(
                                    onSuccess = {
                                        Log.d("Payment", "Firestore update success for formId=$currentFormId")
                                        onPaymentUpdateSuccess(currentFormId)
                                    },
                                    onFailure = { e ->
                                        Log.e("Payment", "Firestore update failed", e)
                                        _paymentState.value = PaymentState.Error("Payment verified but failed to update status")
                                    }
                                )
                            }
                            is Boolean -> {
                                if (updateResult) {
                                    Log.d("Payment", "Firestore update success (boolean) for formId=$currentFormId")
                                    onPaymentUpdateSuccess(currentFormId)
                                } else {
                                    Log.e("Payment", "Firestore update returned false")
                                    _paymentState.value = PaymentState.Error("Failed to update status")
                                }
                            }
                            else -> {
                                // assume success if no exception was thrown
                                Log.d("Payment", "Firestore update returned ${updateResult?.toString()}")
                                onPaymentUpdateSuccess(currentFormId)
                            }
                        }
                    },
                    onFailure = { throwable ->
                        Log.e("Payment", "Verification call failed", throwable)
                        _paymentState.value = PaymentState.Error(throwable.message ?: "Verification error")
                    }
                )
            } catch (e: Exception) {
                Log.e("Payment", "Unexpected exception in verifyPayment", e)
                _paymentState.value = PaymentState.Error(e.message ?: "Unexpected error")
            }
        }
    }

    private fun onPaymentUpdateSuccess(formId: String) {
        _paymentState.value = PaymentState.Success

        PaymentDataHolder.formId = null
        viewModelScope.launch {
            _eventFlow.emit(PaymentEvent.NavigateToViewTicket)
        }
    }


//    fun verifyPayment(orderId: String, paymentId: String, signature: String) {
//        viewModelScope.launch {
//            val currentFormId = PaymentDataHolder.formId
//            if (!currentFormId.isNullOrEmpty()) {
//                Log.d("Payment", "Form ID: $currentFormId")
//                verifyPaymentUseCase(orderId, paymentId, signature).fold(
//                onSuccess = { success ->
//                    if (success) {
//                        Log.d("Payment", "Before update firebase state")
//                        // Update Firestore
//                        val result = userUpdatePaymentStatusUseCase( currentFormId,"PAID")
//                        result.fold(
//                            onSuccess = {
//                                Log.d("Payment", "Before setting state: ${_paymentState.value}")
//                                _paymentState.value = PaymentState.Success
//                                Log.d("Payment", "After setting state: ${_paymentState.value}")
//
//                            },
//                            onFailure = {
//                                _paymentState.value = PaymentState.Error("Payment verified but failed to update status")
//                            }
//                        )
//                    } else {
//                        _paymentState.value = PaymentState.Error("Verification failed")
//                    }
//                },
//                onFailure = {
//                    _paymentState.value = PaymentState.Error(it.message ?: "Error")
//                }
//            )
//            } else {
//                Log.e("Payment", "Form ID is empty!")
//            }
//        }
fun markPaymentFailed(message: String) {
    _paymentState.value = PaymentState.Error(message)
}
}

    sealed class PaymentEvent {
        object NavigateToViewTicket : PaymentEvent()
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

