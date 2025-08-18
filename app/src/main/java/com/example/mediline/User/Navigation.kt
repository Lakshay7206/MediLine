package com.example.mediline.User

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraph

import androidx.navigation.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.mediline.User.ui.Home.HomeScreen
import com.example.mediline.User.ui.authentication.AuthViewModel
import com.example.mediline.User.ui.authentication.LoginScreen
import com.example.mediline.User.ui.authentication.OtpScreen

import com.example.mediline.User.ui.authentication.SignupScreen
import java.lang.reflect.Modifier


sealed class Screen(val route:String){
    object Login: Screen("login")
    object Signup: Screen("signup")
    object Otp: Screen("otp")
    object Home: Screen("home")
    object Queue: Screen("queue")

}
@Composable
fun NavGraph(navController: NavHostController, authViewModel: AuthViewModel = hiltViewModel()) {
    val uiState by authViewModel.uiState.collectAsState()

    NavHost(navController, startDestination = "login") {
        composable("login") {
            LoginScreen(
                uiState = uiState,
                onSendOtp = { phone, activity ->
                    authViewModel.sendOtp(phone, activity)
                },
                onNavigateOtp = { verificationId ->
                    navController.navigate("otp/$verificationId")
                }
            )
        }

        composable("otp/{verificationId}") { backStackEntry ->
            val verificationId = backStackEntry.arguments?.getString("verificationId") ?: ""
            OtpScreen(
                uiState = uiState,
                onVerifyOtp = { otp ->
                    authViewModel.verifyOtp(verificationId, otp)
                },
                onUserExists = { uid -> navController.navigate("home/$uid") },
                onNewUser = { uid -> navController.navigate("signup/$uid") }
            )
        }

        composable("signup/{uid}") { backStackEntry ->
            val uid = backStackEntry.arguments?.getString("uid") ?: ""
            SignupScreen()
        }

        composable("home/{uid}") { backStackEntry ->
            val uid = backStackEntry.arguments?.getString("uid") ?: ""
            HomeScreen(uid)
        }
    }
}
