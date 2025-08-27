package com.example.mediline.User.ui.payment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.room.FtsOptions
import com.example.mediline.User.data.model.CreateOrderResponse

import com.google.androidbrowserhelper.playbilling.provider.PaymentActivity
import com.razorpay.Checkout
import com.razorpay.PaymentResultListener
import org.json.JSONObject






@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentGatewayScreen(
//    activity : Activity,
    gateways: List<String> = listOf("Razorpay", "Paytm", "UPI"),
    amount: Int = 100, // Example amount
    currency: String = "INR",
    viewModel: PaymentViewModel = hiltViewModel()
) {
    val selectedGateway by viewModel.selectedGateway.collectAsState()
    val paymentState by viewModel.paymentState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Select Payment Method") },
                modifier = Modifier.background(Color.White)
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Choose a payment method",
                style = MaterialTheme.typography.titleMedium
            )

            LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                items(gateways) { gateway ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { viewModel.onGatewaySelected(gateway) },
                        elevation = CardDefaults.cardElevation(6.dp),
                        colors = if (gateway == selectedGateway) CardDefaults.cardColors(containerColor = Color(0xFFE0F7FA))
                        else CardDefaults.cardColors(containerColor = Color.White)
                    ) {
                        Row(
                            modifier = Modifier
                                .padding(16.dp)
                                .fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .background(Color.Gray, shape = CircleShape)
                            )
                            Spacer(modifier = Modifier.width(16.dp))
                            Text(text = gateway, style = MaterialTheme.typography.bodyLarge)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    selectedGateway?.let { viewModel.startPayment(amount, currency) }
                },
                enabled = selectedGateway != null && paymentState !is PaymentState.Loading,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Pay ₹$amount")
            }

            Spacer(modifier = Modifier.height(16.dp))
            val context = LocalContext.current
            val activity = context as? Activity


            when (paymentState) {
                is PaymentState.Loading -> Text("Creating order...", color = Color.Gray)
//                is PaymentState.OrderCreated -> {
//                    val order = (paymentState as PaymentState.OrderCreated).order
//                    LaunchedEffect(order) {
//                        activity?.let {
//                            when (selectedGateway) {
//                                "Razorpay" -> {
//                                    // ✅ Start PaymentActivity instead of opening Razorpay directly here
//                                    val intent =
//                                        Intent(context, com.example.mediline.PaymentActivity::class.java).apply {
//                                            putExtra("ORDER_ID", order.orderId)
//                                            putExtra("AMOUNT", order.amount)
//                                        }
//                                    context.startActivity(intent)
//                                }
//                            }
//                        }
//                    }
//                }
//                    LaunchedEffect(order) {
//                        activity?.let {
//                            when (selectedGateway) {
//                                "Razorpay" -> launchRazorpayCheckout(context, it, order, viewModel)
//                                // Add other gateways here later
//                            }
//                        }
//                    }
//                }

                is PaymentState.OrderCreated -> {
                    val order = (paymentState as PaymentState.OrderCreated).order
                    // Launch Razorpay checkout
                    LaunchedEffect(order) {
                        val checkout = Checkout()
                        checkout.setKeyID("rzp_test_R9cjVF2K3mkxS1")

                        val options = JSONObject().apply {
                            put("name", "MediLine")
                            put("description", "Payment for order ${order.orderId}")
                            put("order_id", order.orderId)
                            put("currency", "INR")
                            put("amount", order.amount)
                        }

                        try {
                            activity?.let {
                                checkout.open(it, options)
                            }// Needs Activity context
                        } catch (e: Exception) {
                            viewModel.markPaymentFailed(e.message ?: "Payment failed")
                        }
                    }
                }

                is PaymentState.Success -> {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                            .background(
                                color = Color(0xFFDFF6DD), // light green background
                                shape = RoundedCornerShape(12.dp)
                            )
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = "Success",
                                tint = Color(0xFF4CAF50), // green
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Payment Successful ✅",
                                color = Color(0xFF2E7D32), // darker green
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 16.sp
                            )
                        }
                    }
                }

                is PaymentState.Error-> {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                            .background(
                                color = Color(0xFFFFE6E6), // light red
                                shape = RoundedCornerShape(12.dp)
                            )
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Close,
                                contentDescription = "Failure",
                                tint = Color(0xFFD32F2F), // red
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Payment Failed ❌",
                                color = Color(0xFFC62828), // darker red
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 16.sp
                            )
                        }
                    }
                }


                else -> {}
            }
        }
    }
}



