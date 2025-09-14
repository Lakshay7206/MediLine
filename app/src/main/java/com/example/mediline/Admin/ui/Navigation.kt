package com.example.mediline.Admin.ui

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.mediline.Admin.ui.AdminCreateTicket.AdminCreateTicketScreen
import com.example.mediline.Admin.ui.CreateDepartment.AdminDepartmentScreen
import com.example.mediline.Admin.ui.Profile.AdminProfileScreen
import com.example.mediline.Admin.ui.auth.AdminLoginScreen
import com.example.mediline.Admin.ui.home.AdminTicketViewModel
import com.example.mediline.Admin.ui.home.TicketDetailScreen
import com.example.mediline.Admin.ui.home.TicketManagementScreen
import com.example.mediline.data.model.Form
import okhttp3.Route

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Home : Screen("home")
    object CreateTicket : Screen("create_ticket")
    object Departments : Screen("departments")
    object Profile : Screen("profile")
    object TicketDetail : Screen("ticket_detail/{ticketId}") {
        fun createRoute(ticketId: String) = "ticket_detail/$ticketId"
    }

}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AdminAppNavigation(navController: NavHostController) {

    NavHost(
        navController = navController,
        startDestination = Screen.Login.route
    ) {
        // 🔹 Login
        composable(Screen.Login.route) {
            AdminLoginScreen(
                onLoginSuccess = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            )
        }

        // 🔹 Home (Ticket Management)
        composable(Screen.Home.route) {
            TicketManagementScreen(
                onBack = { navController.popBackStack() },
                onTicketClick = { ticket ->
                    navController.navigate(Screen.TicketDetail.createRoute(ticket.id))
                }
                ,
                onCreateTicket = { navController.navigate(Screen.CreateTicket.route) },
                onDepartments = { navController.navigate(Screen.Departments.route) },
                onProfile = { navController.navigate(Screen.Profile.route) }
            )
        }

        // ✅ Ticket Detail Screen
        composable(
            route = Screen.TicketDetail.route,
            arguments = listOf(navArgument("ticketId") { type = NavType.StringType })
        ) { backStackEntry ->
            val ticketId = backStackEntry.arguments?.getString("ticketId")
            val viewModel: AdminTicketViewModel = hiltViewModel()
            val uiState by viewModel.uiState.collectAsState()

            val ticket = uiState.tickets.find { it.id == ticketId }

            ticket?.let {
                TicketDetailScreen(
                    ticket = it,
                    onBack = { navController.popBackStack() },
                    departments = uiState.departments,
                )
            }
        }



        // 🔹 Create Ticket
        composable(Screen.CreateTicket.route) {
            AdminCreateTicketScreen(
                onBack = { navController.popBackStack() },
                onHome = { navController.navigate(Screen.Home.route) },
                onDepartments = { navController.navigate(Screen.Departments.route) },
                onProfile = { navController.navigate(Screen.Profile.route) }
            )
        }

        // 🔹 Departments
        composable(Screen.Departments.route) {
            AdminDepartmentScreen(
                //onBack = { navController.popBackStack() },
                onHome = { navController.navigate(Screen.Home.route) },
                onCreateTicket = { navController.navigate(Screen.CreateTicket.route) },
                onProfile = { navController.navigate(Screen.Profile.route) }
            )
        }

        // 🔹 Profile
        composable(Screen.Profile.route) {
            AdminProfileScreen(
                //onBack = { navController.popBackStack() },
                onHome = { navController.navigate(Screen.Home.route) },
                onDepartments = { navController.navigate(Screen.Departments.route) },
                onCreateTicket = { navController.navigate(Screen.CreateTicket.route) },
                navigateToLogin = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Profile.route) { inclusive = true }
                    }
                }
            )
        }

    }
}


