package com.example.mediline.User.ui.payment

import android.app.Activity
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.mediline.User.ui.payment.PaymentUiHelper.startRazorpayCheckout

@Composable
fun PaymentScreen(viewModel: PaymentViewModel= hiltViewModel()) {
    val state = viewModel.uiState.collectAsState().value
    val context = LocalContext.current

    Column(Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        Button(onClick = { viewModel.createOrder(amount = 50000) }) { // ₹500
            Text("Pay ₹500")
        }

        when {
            state.loading -> Text("Creating order...")
            state.orderId != null -> {
                Text("Order Created: ${state.orderId}")
                // Launch Razorpay Checkout
                // Launch Razorpay Checkout when orderId is ready
                LaunchedEffect(state.orderId) {
                    val activity = context as? Activity
                    if (activity != null) {
                        PaymentUiHelper.startRazorpayCheckout(
                            activity,
                            state.orderId,
                            50000
                        )
                    } else {
                        Toast.makeText(context, "Not an activity context", Toast.LENGTH_SHORT).show()
                    }
                }
            }

            state.error != null -> Text("Error: ${state.error}")
        }
    }
}
