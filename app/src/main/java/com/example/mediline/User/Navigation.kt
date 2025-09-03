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
    object Signup: Screen("signup")
    object Otp: Screen("otp")
    object Home: Screen("home")
    object Queue: Screen("queue/{departmentId}"){
        fun createRoute(departmentId: String) = "queue/$departmentId"
    }
    object CreateTicket: Screen("createTicket/{departmentId}") {
        fun createRoute(departmentId: String) = "createTicket/$departmentId"
    }
    object ViewTicket: Screen("viewTicket/{departmentId}") {
        fun createRoute(departmentId: String) = "viewTicket/$departmentId"
    }

    object PaymentGateway: Screen("paymentGateway")
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
                    rootNavController.navigate("home_main") {
                        popUpTo("auth") { inclusive = true }
                    }
                },

                onNewUser = { rootNavController.navigate("signup") },
                //onResendOtp = { /*TODO*/ }
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
                    rootNavController.navigate("home_main") {
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
            HomeScreen(
                onDepartmentClick = { deptId->
                    rootNavController.navigate("queue/${deptId}")
                }
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
                { rootNavController.navigate("viewTicket/$deptId") }
            )
        }

        composable(
            "createTicket/{departmentId}",
            arguments = listOf(navArgument("departmentId") { type = NavType.StringType })
        ) { backStackEntry ->
            val deptId = backStackEntry.arguments?.getString("departmentId")!!
            RegistrationScreen(deptId, { rootNavController.navigate("paymentGateway") })
        }

        composable(
            "viewTicket/{departmentId}",
            arguments = listOf(navArgument("departmentId") { type = NavType.StringType })
        ) { backStackEntry ->
            val deptId = backStackEntry.arguments?.getString("departmentId")!!
           TicketScreen()
        }

        composable("paymentGateway"){
            PaymentGatewayScreen()

        }
    }
}

//fun NavGraphBuilder.homeNavGraph(rootNavController: NavHostController) {
//    navigation(
//        startDestination = "home",
//        route = "home_main"
//    ) {
//        composable("home") {
//            HomeScreen()
//        }
//        composable("queue",
//                arguments = listOf(navArgument("departmentId") { type = NavType.StringType })) { backStackEntry ->
//              val deptId = backStackEntry.arguments?.getString("departmentId")!!
//
//            QueueScreen(deptId,{rootNavController.navigate("createTicket/${deptId}")},{rootNavController.navigate(
//                "viewTicket/${deptId}")})
//        }
//        composable("createTicket",listOf(navArgument("departmentId") { type = NavType.StringType })) {backStackEntry ->
//            val deptId = backStackEntry.arguments?.getString("departmentId")!!
//            RegistrationScreen(deptId,"","",0.0)
//        }
//        composable("viewTicket",listOf(navArgument("departmentId") { type = NavType.StringType })) {
//            ViewTicketsScreen()
//        }
//
//    }


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
