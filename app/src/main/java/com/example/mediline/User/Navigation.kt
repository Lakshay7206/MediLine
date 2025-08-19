package com.example.mediline.User

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder

import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.example.mediline.User.ui.Home.HomeScreen
import com.example.mediline.User.ui.authentication.AuthViewModel
import com.example.mediline.User.ui.authentication.LoginScreen
import com.example.mediline.User.ui.authentication.OtpScreen

import com.example.mediline.User.ui.authentication.SignupScreen


sealed class Screen(val route:String){
    object Login: Screen("login")
    object Signup: Screen("signup")
    object Otp: Screen("otp")
    object Home: Screen("home")
    object Queue: Screen("queue")

}
@Composable
fun RootNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = "auth"
    ) {
        authNavGraph(navController)
        homeNavGraph(navController)
    }
}

fun NavGraphBuilder.authNavGraph(rootNavController: NavHostController) {
    navigation(
        startDestination = "login",
        route = "auth"
    ) {
        composable("login") { backStackEntry ->
            val parentEntry = remember(backStackEntry) {
                rootNavController.getBackStackEntry("auth")
            }
            val viewModel: AuthViewModel = hiltViewModel(parentEntry)
            LoginScreen(
viewModel,
                onNavigateOtp = { rootNavController.navigate("otp") },

            )
        }
        composable("otp") { backStackEntry ->
            val parentEntry = remember(backStackEntry) {
                rootNavController.getBackStackEntry("auth")
            }
            val viewModel: AuthViewModel = hiltViewModel(parentEntry)

            OtpScreen(
                viewModel  ,
                onUserExists = {
                    // ✅ Jump to home graph
                    rootNavController.navigate("home") {
                        popUpTo("auth") { inclusive = true }
                    }
                },
                onNewUser = { rootNavController.navigate("signup") }
            )
        }
        composable("signup") { backStackEntry ->
            val parentEntry = remember(backStackEntry) {
                rootNavController.getBackStackEntry("auth")
            }
            val viewModel: AuthViewModel = hiltViewModel(parentEntry)

            SignupScreen(
                viewModel  ,
                navigateHome = {
                    rootNavController.navigate("home") {
                        popUpTo("auth") { inclusive = true }
                    }
                }
            )
        }
    }
}

fun NavGraphBuilder.homeNavGraph(rootNavController: NavHostController) {
    navigation(
        startDestination = "home",
        route = "home_main"
    ) {
        composable("home") {
            HomeScreen()


        }
    }
}

//@SuppressLint("UnrememberedGetBackStackEntry")
//@Composable
//fun NavGraph(navController: NavHostController) {
//
//
//    NavHost(navController, startDestination = "login",route = "auth") {
//        composable("login") {
//            LoginScreen(
//                onNavigateOtp = {
//                    navController.navigate("otp")
//                },
//            )
//        }
//
//        composable("otp") {
//
//            OtpScreen(
//                onUserExists = {navController.navigate("home") },
//                onNewUser = {navController.navigate("signup") }
//            )
//        }
//
//        composable("signup") {
//
//            SignupScreen( navigateHome = {
//                 navController.navigate("home")
//                })}
//
//        composable("home") {
//
//                    HomeScreen()
//                }
//
//    }
//}
