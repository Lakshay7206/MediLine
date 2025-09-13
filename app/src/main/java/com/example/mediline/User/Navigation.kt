package com.example.mediline.User


import TicketScreen
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder

import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.navArgument
import com.example.mediline.User.ui.Home.HomeScreen
import com.example.mediline.User.ui.Queue.QueueScreen
import com.example.mediline.User.ui.authentication.AuthViewModel
import com.example.mediline.User.ui.authentication.LoginScreen
import com.example.mediline.User.ui.authentication.OtpScreen

import com.example.mediline.User.ui.authentication.SignupScreen
import com.example.mediline.User.ui.createTicket.RegistrationScreen
import com.example.mediline.User.ui.payment.PaymentGatewayScreen


sealed class Screen(val route:String){
    object Login: Screen("login")
    object Signup: Screen("signUp")
    object Otp: Screen("otp")
    object Home: Screen("home")
    object Queue: Screen("queue/{departmentId}"){
        fun createRoute(departmentId: String) = "queue/$departmentId"
    }
    object CreateTicket: Screen("createTicket/{departmentId}") {
        fun createRoute(departmentId: String) = "createTicket/$departmentId"
    }
    object ViewTicket: Screen("viewTicket")

    object PaymentGateway: Screen("paymentGateway/{formId}"){
        fun createRoute(formId: String) = "viewTicket/$formId"


    }
}
@Composable
fun RootNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = "home_main"
    ) {
        authNavGraph(navController)
        homeNavGraph(navController)
    }
}

fun NavGraphBuilder.authNavGraph(rootNavController: NavHostController) {
    navigation(
        startDestination = "home",
        route = "home_main"
    ) {
//        composable("login") { backStackEntry ->
//            val parentEntry = remember(backStackEntry) {
//                rootNavController.getBackStackEntry("auth")
//            }
//            val viewModel: AuthViewModel = hiltViewModel(parentEntry)
//            LoginScreen(
//                viewModel,
//                onNavigateOtp = { rootNavController.navigate("otp") },
//                {rootNavController.navigate("signUp")}
//
//
//
//            )
//        }
        composable("otp") { backStackEntry ->
            val parentEntry = remember(backStackEntry) {
                rootNavController.getBackStackEntry("auth")
            }
            val viewModel: AuthViewModel = hiltViewModel(parentEntry)

            OtpScreen(
                viewModel  ,
                onUserExists = {
                    rootNavController.navigate("home_main") {
                        popUpTo("auth") { inclusive = true }
                    }
                },

                onNewUser = { rootNavController.navigate("signUp") },
                {rootNavController.navigate(Screen.Login.route)}
                //onResendOtp = { /*TODO*/ }
            )
        }
        composable("signUp") { backStackEntry ->
            val parentEntry = remember(backStackEntry) {
                rootNavController.getBackStackEntry("auth")
            }
            val viewModel: AuthViewModel = hiltViewModel(parentEntry)

            SignupScreen(
                viewModel,
                navigateHome = {
                    rootNavController.navigate("home_main") {
                        popUpTo("auth") { inclusive = true }
                    }
                },
                navigateBack = {rootNavController.navigate(Screen.Login.route)}
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
            HomeScreen(
                onDepartmentClick = { deptId->
                    rootNavController.navigate("queue/${deptId}")
                },
                {rootNavController.navigate(Screen.ViewTicket.route)}
            )
        }

        composable(
            "queue/{departmentId}",
            arguments = listOf(navArgument("departmentId") { type = NavType.StringType })
        ) { backStackEntry ->
            val deptId = backStackEntry.arguments?.getString("departmentId")!!
            QueueScreen(
                deptId,
                { rootNavController.navigate("createTicket/$deptId") },
                { rootNavController.navigate("viewTicket") },
                {rootNavController.popBackStack()}
            )
        }

//        composable(
//            "createTicket/{departmentId}",
//            arguments = listOf(navArgument("departmentId") { type = NavType.StringType })
//        ) { backStackEntry ->
//            val deptId = backStackEntry.arguments?.getString("departmentId")!!
//            RegistrationScreen(deptId, { formId->
//                rootNavController.navigate("paymentGateway/$formId") },
//                {rootNavController.popBackStack()})
//        }

        composable(
            "viewTicket"
        ) {
           TicketScreen({rootNavController.navigate(Screen.Home.route)})
        }

        composable(
            "createTicket/{departmentId}",
            arguments = listOf(navArgument("departmentId") { type = NavType.StringType })
        ) { backStackEntry ->
            val deptId = backStackEntry.arguments?.getString("departmentId")!!
            RegistrationScreen(
                deptId,
                { formId -> rootNavController.navigate("paymentGateway/$formId") },
                { rootNavController.popBackStack() }
            )
        }

        composable(
            "paymentGateway/{formId}",
            arguments = listOf(navArgument("formId") { type = NavType.StringType })
        ) { backStackEntry ->
            val formId = backStackEntry.arguments?.getString("formId")!!
            PaymentGatewayScreen(formId, { rootNavController.navigate(Screen.CreateTicket.route) }, navController = rootNavController)
        }

    }
}

