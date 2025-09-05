package com.example.mediline.User.ui.payment

import android.app.Activity
import android.util.Log
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.mediline.Admin.ui.AdminCreateTicket.CurvedTopBar
import com.example.mediline.User.Screen
import com.example.mediline.User.ui.authentication.CurvedTopBar
import com.example.mediline.User.ui.theme.BackgroundDark
import com.example.mediline.User.ui.theme.Black
import com.example.mediline.User.ui.theme.PrimaryGreen
import com.example.mediline.User.ui.theme.TextGray
import com.example.mediline.User.ui.theme.White
import com.example.mediline.data.repo.PaymentDataHolder
import com.razorpay.Checkout
import org.json.JSONObject
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentGatewayScreen(
    formId: String,
    navigateBack: () -> Unit,
    gateways: List<String> = listOf("Razorpay", "Paytm", "UPI"),
    amount: Int = 100,
    currency: String = "INR",
    navController: NavController,
    viewModel: PaymentViewModel = hiltViewModel()
) {
    val selectedGateway by viewModel.selectedGateway.collectAsState()
    val paymentState by viewModel.paymentState.collectAsState()

 //   val formState by viewModel.formId.collectAsState()
    Log.d("Payment", "Form ID: $formId")

    LaunchedEffect(paymentState) {
        if (paymentState is PaymentState.Success) {
            navController.navigate(Screen.ViewTicket.route) {
                popUpTo(Screen.PaymentGateway.route) { inclusive = true }
            }
        }
    }


    val context = LocalContext.current
    val activity = context as? Activity

    Scaffold(
        topBar = {
            CurvedTopBar("PaymentGateways", true, navigateBack)
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .background(Color(0xFFF9FAFB)) // light gray background
                .padding(horizontal = 20.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // Title
            Text(
                text = "Choose a payment method",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                color = Color(0xFF111827) // dark gray
            )

            // Gateways list with card style
            LazyColumn(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                items(gateways) { gateway ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { viewModel.onGatewaySelected(gateway) },
                        colors = if (gateway == selectedGateway) {
                            CardDefaults.cardColors(containerColor = Color(0xFFF1FDF6)) // soft green tint
                        } else {
                            CardDefaults.cardColors(containerColor = Color.White)
                        },
                        shape = RoundedCornerShape(16.dp),
                        border = BorderStroke(
                            width = if (gateway == selectedGateway) 2.dp else 1.dp,
                            color = if (gateway == selectedGateway) PrimaryGreen else Color(0xFFE0E0E0)
                        ),
                        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .padding(16.dp)
                                .fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(48.dp)
                                    .background(Color(0xFFF1FDF6), shape = CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = when (gateway) {
                                        "Razorpay" -> Icons.Default.CreditCard
                                        "Paytm" -> Icons.Default.AccountBalanceWallet
                                        else -> Icons.Default.QrCode
                                    },
                                    contentDescription = gateway,
                                    tint = PrimaryGreen,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(16.dp))
                            Text(
                                text = gateway,
                                style = MaterialTheme.typography.titleMedium,
                                color = Color(0xFF333333)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Pay Button
            Button(
                onClick = {
                    PaymentDataHolder.formId = formId
                    selectedGateway?.let { viewModel.startPayment(amount, currency) }
                },
                enabled = selectedGateway != null && paymentState !is PaymentState.Loading,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = PrimaryGreen,
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(Icons.Default.CheckCircle, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text("Pay ₹$amount", fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
            }

            // Payment states
            when (paymentState) {
                is PaymentState.Loading -> StatusCard(
                    message = "Creating order...",
                    icon = Icons.Default.Schedule,
                    background = Color(0xFFE8F5E9),
                    iconTint = PrimaryGreen,
                    textColor = PrimaryGreen
                )

                is PaymentState.OrderCreated -> {
                    val order = (paymentState as PaymentState.OrderCreated).order
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
                            activity?.let { checkout.open(it, options) }
                        } catch (e: Exception) {
                            viewModel.markPaymentFailed(e.message ?: "Payment failed")
                        }
                    }
                }

                is PaymentState.Success -> StatusCard(
                    message = "Payment Successful ✅",
                    icon = Icons.Default.CheckCircle,
                    background = Color(0xFFD1FAE5),
                    iconTint = Color(0xFF059669),
                    textColor = Color(0xFF065F46)
                )

                is PaymentState.Error -> StatusCard(
                    message = "Payment Failed ❌",
                    icon = Icons.Default.Close,
                    background = Color(0xFFFEE2E2),
                    iconTint = Color(0xFFDC2626),
                    textColor = Color(0xFFB91C1C)
                )

                else -> {}
            }
        }
    }
}

@Composable
fun StatusCard(
    message: String,
    icon: ImageVector,
    background: Color,
    iconTint: Color,
    textColor: Color
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        colors = CardDefaults.cardColors(containerColor = background),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = iconTint,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = message,
                color = textColor,
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp
            )
        }
    }
}


//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun PaymentGatewayScreen(
//    navigateBack: () -> Unit,
//    gateways: List<String> = listOf("Razorpay", "Paytm", "UPI"),
//    amount: Int = 100, // Example amount
//    currency: String = "INR",
//    viewModel: PaymentViewModel = hiltViewModel()
//) {
//    val selectedGateway by viewModel.selectedGateway.collectAsState()
//    val paymentState by viewModel.paymentState.collectAsState()
//
//    Scaffold(
//        topBar = {
//            TopAppBar(
//                title = { Text("Select Payment Method") },
//                modifier = Modifier.background(Color.White)
//            )
//        }
//    ) { padding ->
//        Column(
//            modifier = Modifier
//                .padding(padding)
//                .fillMaxSize()
//                .padding(16.dp),
//            verticalArrangement = Arrangement.spacedBy(12.dp)
//        ) {
//            Text(
//                text = "Choose a payment method",
//                style = MaterialTheme.typography.titleMedium
//            )
//
//            LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
//                items(gateways) { gateway ->
//                    Card(
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .clickable { viewModel.onGatewaySelected(gateway) },
//                        elevation = CardDefaults.cardElevation(6.dp),
//                        colors = if (gateway == selectedGateway) CardDefaults.cardColors(containerColor = Color(0xFFE0F7FA))
//                        else CardDefaults.cardColors(containerColor = Color.White)
//                    ) {
//                        Row(
//                            modifier = Modifier
//                                .padding(16.dp)
//                                .fillMaxWidth(),
//                            verticalAlignment = Alignment.CenterVertically
//                        ) {
//                            Box(
//                                modifier = Modifier
//                                    .size(40.dp)
//                                    .background(Color.Gray, shape = CircleShape)
//                            )
//                            Spacer(modifier = Modifier.width(16.dp))
//                            Text(text = gateway, style = MaterialTheme.typography.bodyLarge)
//                        }
//                    }
//                }
//            }
//
//            Spacer(modifier = Modifier.height(24.dp))
//
//            Button(
//                onClick = {
//                    selectedGateway?.let { viewModel.startPayment(amount, currency) }
//                },
//                enabled = selectedGateway != null && paymentState !is PaymentState.Loading,
//                modifier = Modifier.fillMaxWidth()
//            ) {
//                Text("Pay ₹$amount")
//            }
//
//            Spacer(modifier = Modifier.height(16.dp))
//            val context = LocalContext.current
//            val activity = context as? Activity
//
//
//            when (paymentState) {
//                is PaymentState.Loading -> Text("Creating order...", color = Color.Gray)
////                is PaymentState.OrderCreated -> {
////                    val order = (paymentState as PaymentState.OrderCreated).order
////                    LaunchedEffect(order) {
////                        activity?.let {
////                            when (selectedGateway) {
////                                "Razorpay" -> {
////                                    // ✅ Start PaymentActivity instead of opening Razorpay directly here
////                                    val intent =
////                                        Intent(context, com.example.mediline.PaymentActivity::class.java).apply {
////                                            putExtra("ORDER_ID", order.orderId)
////                                            putExtra("AMOUNT", order.amount)
////                                        }
////                                    context.startActivity(intent)
////                                }
////                            }
////                        }
////                    }
////                }
////                    LaunchedEffect(order) {
////                        activity?.let {
////                            when (selectedGateway) {
////                                "Razorpay" -> launchRazorpayCheckout(context, it, order, viewModel)
////                                // Add other gateways here later
////                            }
////                        }
////                    }
////                }
//
//                is PaymentState.OrderCreated -> {
//                    val order = (paymentState as PaymentState.OrderCreated).order
//                    // Launch Razorpay checkout
//                    LaunchedEffect(order) {
//                        val checkout = Checkout()
//                        checkout.setKeyID("rzp_test_R9cjVF2K3mkxS1")
//
//                        val options = JSONObject().apply {
//                            put("name", "MediLine")
//                            put("description", "Payment for order ${order.orderId}")
//                            put("order_id", order.orderId)
//                            put("currency", "INR")
//                            put("amount", order.amount)
//                        }
//
//                        try {
//                            activity?.let {
//                                checkout.open(it, options)
//                            }// Needs Activity context
//                        } catch (e: Exception) {
//                            viewModel.markPaymentFailed(e.message ?: "Payment failed")
//                        }
//                    }
//                }
//
//                is PaymentState.Success -> {
//                    Box(
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .padding(16.dp)
//                            .background(
//                                color = Color(0xFFDFF6DD), // light green background
//                                shape = RoundedCornerShape(12.dp)
//                            )
//                            .padding(16.dp),
//                        contentAlignment = Alignment.Center
//                    ) {
//                        Row(
//                            verticalAlignment = Alignment.CenterVertically,
//                            horizontalArrangement = Arrangement.Center
//                        ) {
//                            Icon(
//                                imageVector = Icons.Default.CheckCircle,
//                                contentDescription = "Success",
//                                tint = Color(0xFF4CAF50), // green
//                                modifier = Modifier.size(24.dp)
//                            )
//                            Spacer(modifier = Modifier.width(8.dp))
//                            Text(
//                                text = "Payment Successful ✅",
//                                color = Color(0xFF2E7D32), // darker green
//                                fontWeight = FontWeight.SemiBold,
//                                fontSize = 16.sp
//                            )
//                        }
//                    }
//                }
//
//                is PaymentState.Error-> {
//                    Box(
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .padding(16.dp)
//                            .background(
//                                color = Color(0xFFFFE6E6), // light red
//                                shape = RoundedCornerShape(12.dp)
//                            )
//                            .padding(16.dp),
//                        contentAlignment = Alignment.Center
//                    ) {
//                        Row(
//                            verticalAlignment = Alignment.CenterVertically,
//                            horizontalArrangement = Arrangement.Center
//                        ) {
//                            Icon(
//                                imageVector = Icons.Filled.Close,
//                                contentDescription = "Failure",
//                                tint = Color(0xFFD32F2F), // red
//                                modifier = Modifier.size(24.dp)
//                            )
//                            Spacer(modifier = Modifier.width(8.dp))
//                            Text(
//                                text = "Payment Failed ❌",
//                                color = Color(0xFFC62828), // darker red
//                                fontWeight = FontWeight.SemiBold,
//                                fontSize = 16.sp
//                            )
//                        }
//                    }
//                }
//
//
//                else -> {}
//            }
//        }
//    }
//}
//


