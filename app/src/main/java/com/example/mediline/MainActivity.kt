package com.example.mediline

import TicketScreen
import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.navigation.compose.rememberNavController
import com.example.mediline.User.RootNavGraph
import com.example.mediline.User.ui.Home.HomeScreen
import com.example.mediline.User.ui.payment.PaymentViewModel
import com.example.mediline.User.ui.theme.AppTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlin.getValue
import com.razorpay.PaymentData
import com.razorpay.PaymentResultWithDataListener

@AndroidEntryPoint
class MainActivity : ComponentActivity() , PaymentResultWithDataListener{
    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("ViewModelConstructorInComposable")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppTheme {
                val navController = rememberNavController()
              RootNavGraph(navController)
              // OtpScreen({},{},{})

               // InviteAdminScreen()
                //AdminLoginScreen({Log.d("TAG", "onCreate:successful")})
              // PaymentGatewayScreen({})
               // TicketManagementScreen({},{})
               // AdminCreateTicketScreen()
          //  QueueScreen("1",{},{},{})
//                TicketScreen(
//                    navigateHome ={}
//                )
              //  AdminCreateTicketScreen()
                //TicketManagementScreen({})
                //AcceptInviteScreen("")
                 //AdminDepartmentScreen()
               // RegistrationScreen("1",{},{})
               // HomeScreen({},{})
                //HomeScreen({},{})
               // PaymentGatewayScreen({})
                //QueueScreen("",{},{},{})
               // TicketScreen({})
               // AcceptInviteScreen("")
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
            viewModel.verifyPayment(orderId, paymentId, signature )
        }
    }

    // Called by Razorpay when payment fails
    override fun onPaymentError(code: Int, description: String?, paymentData: PaymentData?) {
        viewModel.markPaymentFailed(description ?: "Unknown error")
    }
}


