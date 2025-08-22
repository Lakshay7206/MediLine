package com.example.mediline.User.ui.payment

import android.app.Activity
import com.razorpay.Checkout
import org.json.JSONObject


object PaymentUiHelper {
    fun startRazorpayCheckout(activity: Activity, orderId: String, amount: Int) {
        val checkout = Checkout()
        checkout.setKeyID("YOUR_RAZORPAY_KEY_ID")

        val options = JSONObject()
        options.put("name", "MediLine")
        options.put("description", "Payment for appointment")
        options.put("order_id", orderId)
        options.put("currency", "INR")
        options.put("amount", amount) // amount in paise

        checkout.open(activity, options)
    }
}
