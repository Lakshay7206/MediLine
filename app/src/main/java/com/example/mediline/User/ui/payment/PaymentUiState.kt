package com.example.mediline.User.ui.payment

import com.example.mediline.data.model.CreateOrderResponse

sealed class PaymentState{
    object Loading: PaymentState()
    object Idle: PaymentState()
    data class OrderCreated(val order: CreateOrderResponse): PaymentState()

    data  class Error(val msg: String): PaymentState()
    object Success: PaymentState()
}