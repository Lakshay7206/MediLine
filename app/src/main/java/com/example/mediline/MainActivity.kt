package com.example.mediline

import TicketScreen
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.mediline.Admin.ui.CreateDepartment.AdminDepartmentScreen
import com.example.mediline.User.RootNavGraph
import com.example.mediline.User.Screen
import com.example.mediline.User.ui.payment.PaymentViewModel
import com.example.mediline.User.ui.theme.AppTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlin.getValue
import com.razorpay.PaymentData
import com.razorpay.PaymentResultListener
import com.razorpay.PaymentResultWithDataListener







@AndroidEntryPoint
class MainActivity : ComponentActivity(), PaymentResultWithDataListener {

    private lateinit var navController: NavHostController
    private val viewModel: PaymentViewModel by viewModels()

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("ViewModelConstructorInComposable")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppTheme {
                 navController = rememberNavController()
                RootNavGraph(navController)
                //AdminDepartmentScreen()

            }
        }
    }


    // Razorpay success callback
    override fun onPaymentSuccess(razorpayPaymentId: String?, paymentData: PaymentData?) {
        try {
            val paymentId = paymentData?.paymentId
            val orderId = paymentData?.orderId
            val signature = paymentData?.signature

            // Navigate to ViewTicket screen
            navController.navigate(Screen.ViewTicket.route) {
                popUpTo(Screen.PaymentGateway.route) { inclusive = true }
                launchSingleTop = true
            }

            // Optional: verify payment with backend
        if (paymentId != null && orderId != null && signature != null) {
            viewModel.verifyPayment(orderId, paymentId, signature)
        }

        } catch (e: Exception) {
            e.printStackTrace()
            // log or show error (Toast/snackbar)
            Log.d("Payment", "Navigation failed: ${e.message}")
            Toast.makeText(this, "Navigation failed: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }


    // Razorpay error callback
        override fun onPaymentError(code: Int, description: String?, paymentData: PaymentData?) {
            viewModel.markPaymentFailed(description ?: "Unknown error")
        }

        // Razorpay returns here if activity is already running
        override fun onNewIntent(intent: Intent) {
            super.onNewIntent(intent)
            setIntent(intent) // ensures the existing activity receives Razorpay result
        }

    }







