package com.example.mediline

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.rememberNavController
import com.example.mediline.Admin.ui.AdminCreateTicket.AdminCreateTicketScreen
import com.example.mediline.Admin.ui.auth.AdminLoginScreen

import com.example.mediline.Admin.ui.home.TicketManagementScreen
import com.example.mediline.User.RootNavGraph
import com.example.mediline.User.ui.payment.PaymentGatewayScreen

import com.example.mediline.User.ui.payment.PaymentGatewayScreen

import com.example.mediline.User.ui.payment.PaymentViewModel
import com.example.mediline.User.ui.theme.MediLineTheme
import com.example.mediline.superAdmin.InviteAdminScreen

import com.razorpay.PaymentResultListener
import dagger.hilt.android.AndroidEntryPoint
import org.json.JSONObject
import kotlin.getValue
import com.razorpay.PaymentData
import com.razorpay.PaymentResultWithDataListener
import com.razorpay.Checkout
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() , PaymentResultWithDataListener{
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MediLineTheme {
                val navController = rememberNavController()
              // RootNavGraph(navController)
               // InviteAdminScreen()
                AdminLoginScreen({Log.d("TAG", "onCreate:successful")})
               //PaymentGatewayScreen()
               // TicketManagementScreen({},{})
               // AdminCreateTicketScreen()



            }
        }
    }
    private val viewModel: PaymentViewModel by viewModels()
    // Called by Razorpay when payment succeeds
    override fun onPaymentSuccess(razorpayPaymentId: String?, paymentData: PaymentData?) {
        val paymentId = paymentData?.paymentId
        val orderId = paymentData?.orderId
        val signature = paymentData?.signature

        if (paymentId != null && orderId != null && signature != null) {
            viewModel.verifyPayment(orderId, paymentId, signature)
        }
    }

    // Called by Razorpay when payment fails
    override fun onPaymentError(code: Int, description: String?, paymentData: PaymentData?) {
        viewModel.markPaymentFailed(description ?: "Unknown error")
    }
}

//@AndroidEntryPoint
//class PaymentActivity : ComponentActivity(), PaymentResultWithDataListener {
//
//    private val viewModel: PaymentViewModel by viewModels()
//    private lateinit var orderId: String
//    private var amount: Int = 0
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//
//        orderId = intent.getStringExtra("ORDER_ID") ?: ""
//        amount = intent.getIntExtra("AMOUNT", 0)
//
//        startRazorpayCheckout(orderId, amount)
//    }
//
//    private fun startRazorpayCheckout(orderId: String, amount: Int) {
//        val checkout = Checkout()
//        checkout.setKeyID("rzp_test_R9cjVF2K3mkxS1")
//
//        val options = JSONObject().apply {
//            put("name", "MediLine")
//            put("description", "Payment for order $orderId")
//            put("order_id", orderId)
//            put("currency", "INR")
//            put("amount", amount)
//        }
//
//        checkout.open(this, options)
//    }
//
//    override fun onPaymentSuccess(razorpayPaymentId: String?, paymentData: PaymentData?) {
//
//            val paymentId = paymentData?.paymentId
//            val razorpayOrderId = paymentData?.orderId
//            val signature = paymentData?.signature
//
//            Log.e("Razorpay", "PaymentId: $paymentId")
//            Log.e("Razorpay", "OrderId: $razorpayOrderId")
//            Log.e("Razorpay", "Payment Signature: $signature")
//
//            if (paymentId != null && razorpayOrderId != null && signature != null) {
//                lifecycleScope.launch {
//                    try {
//                        val success = viewModel.verifyPayment(  orderId = razorpayOrderId, paymentId = paymentId, signature = signature)
//                        Log.e("Razorpay", "Backend verification success: $success")
//                        finish()
//                    } catch (e: Exception) {
//                        Log.e("Razorpay", "Backend verification failed: ${e.message}")
//                    }
//            }
//            Log.e("Razorpay", "Sent to backend for verification")
//        }
//
//    }
//
//    override fun onPaymentError(code: Int, description: String?, paymentData: PaymentData?) {
//        viewModel.markPaymentFailed(description ?: "Unknown error")
//        Log.e("Razorpay", "Payment failed: $description ($code)")
//        finish()
//    }
//
//
//
//}


