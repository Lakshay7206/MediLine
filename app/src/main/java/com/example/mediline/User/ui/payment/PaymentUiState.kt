package com.example.mediline.User.ui.payment

data class PaymentUiState(
    val loading: Boolean = false,
    val orderId: String? = null,
    val error: String? = null,
    val verify: Boolean=false
)