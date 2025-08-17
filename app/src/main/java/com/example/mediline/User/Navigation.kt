package com.example.mediline.User

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraph

import androidx.navigation.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.mediline.User.ui.Home.HomeScreen
import com.example.mediline.User.ui.authentication.LoginScreen
import com.example.mediline.User.ui.authentication.OtpScreen
import com.example.mediline.User.ui.authentication.SignUpScreen
import java.lang.reflect.Modifier


sealed class Screen(val route:String){
    object Login: Screen("login")
    object Signup: Screen("signup")
    object Otp: Screen("otp")
    object Home: Screen("home")
    object Queue: Screen("queue")

}
@Composable
fun NavGraph(navController: NavHostController,modifier: Modifier){
    NavHost(navController = navController, startDestination = Screen.Login.route){
        composable(Screen.Login.route){
            LoginScreen({},navController)
        }
        composable(Screen.Signup.route){
            SignUpScreen(navController = navController)
        }
        composable (Screen.Otp.route){
            OtpScreen({},navController)
        }
        composable (Screen.Home.route){
            HomeScreen()
        }



    }

}