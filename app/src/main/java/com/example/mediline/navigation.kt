package com.example.mediline

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.example.mediline.Admin.ui.AdminAppNavigation
import com.example.mediline.User.authNavGraph
import com.example.mediline.User.homeNavGraph

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MainNavHost(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = "entry"
    ) {
        composable("entry") {
            EntryScreen(navController)
        }

        // User flow
        navigation(
            startDestination = "auth",
            route = "root_graph"
        ) {
            authNavGraph(navController)
            homeNavGraph(navController)
        }



        composable("admin_graph") {
            // ✅ Create a separate controller for Admin graph
            val adminNavController = rememberNavController()
            AdminAppNavigation(adminNavController)
        }

        }
    }

