//package com.example.mediline.User.ui.authentication
//
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.layout.Column
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.collectAsState
//import androidx.compose.runtime.getValue
//import androidx.hilt.navigation.compose.hiltViewModel
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.Row
//import androidx.compose.foundation.layout.Spacer
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.layout.size
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material.icons.Icons
//import androidx.compose.material3.Button
//import androidx.compose.material3.Divider
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.Switch
//import androidx.compose.material3.Text
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.unit.dp
//
//im
//
//@Composable
//fun ProfileScreen(
//    profileViewModel: AuthViewModel= hiltViewModel(),
//    onNavigateLogin: () -> Unit
//) {
//    val user by profileViewModel.formState.collectAsState()
//
//
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .padding(24.dp)
//    ) {
//        // 1️⃣ Profile Picture + Name + Email
//        if (user != null) {
//
//            Text(
//                text = user.phone,
//                style = MaterialTheme.typography.bodyMedium,
//                color = Color.Gray
//            )
//        }
//
//        Spacer(modifier = Modifier.height(32.dp))
//
//        // 2️⃣ Notifications Toggle
//        Row(
//            modifier = Modifier
//                .fillMaxWidth()
//                .clickable { profileViewModel.toggleNotifications() }
//                .padding(vertical = 16.dp),
//            horizontalArrangement = Arrangement.SpaceBetween,
//            verticalAlignment = Alignment.CenterVertically
//        ) {
//            Text("Notifications", style = MaterialTheme.typography.bodyLarge)
//            val notificationsEnabled = false
//            Switch(
//                checked = notificationsEnabled,
//                onCheckedChange = { profileViewModel.toggleNotifications() }
//            )
//        }
//
//        Divider()
//
//        Spacer(modifier = Modifier.height(24.dp))
//
//        // 3️⃣ Logout Button
//        Button(
//            onClick = {
//                profileViewModel.logout()
//                onNavigateLogin()
//            },
//            modifier = Modifier.fillMaxWidth(),
//            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
//        ) {
//            Text("Log Out", color = MaterialTheme.colorScheme.onPrimary)
//        }
//    }
//}
