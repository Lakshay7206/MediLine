package com.example.mediline

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import com.example.mediline.User.RootNavGraph

import com.example.mediline.User.ui.Home.HomeScreen
import com.example.mediline.User.ui.authentication.AuthViewModel
import com.example.mediline.User.ui.authentication.LoginScreen
import com.example.mediline.User.ui.createTicket.RegistrationScreen
import com.example.mediline.User.ui.payment.PaymentScreen
import com.example.mediline.User.ui.theme.MediLineTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MediLineTheme {

                val navController = rememberNavController()
                RootNavGraph(navController)

//                val navController = rememberNavController()
//
//                RootNavGraph(navController)
               // RegistrationScreen("abc","abc",123.0)
                HomeScreen({})
            }
        }
    }
}

