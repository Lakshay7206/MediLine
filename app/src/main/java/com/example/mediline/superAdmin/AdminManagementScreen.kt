package com.example.mediline.superAdmin

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun InviteAdminScreen(viewModel: AdminViewModel = hiltViewModel()) {
    var email by remember { mutableStateOf("") }
    val state by viewModel.uiState.collectAsState()

    Column(Modifier.padding(16.dp)) {
        TextField(value = email, onValueChange = { email = it }, label = { Text("Admin Email") })
        Spacer(Modifier.height(16.dp))
        Button(onClick = { viewModel.sendInvite(email) }) {
            Text("Send Invite")
        }

        when (state) {
            is AdminUiState.Loading -> Text("Sending invite...")
            is AdminUiState.Success -> Text((state as AdminUiState.Success).message)
            is AdminUiState.Error -> Text((state as AdminUiState.Error).message, color = Color.Red)
            else -> {}
        }
    }
}
@Composable
fun AcceptInviteScreen(
    token: String,
    viewModel: AdminViewModel = hiltViewModel()
) {
    var password by remember { mutableStateOf("") }
    val state by viewModel.uiState.collectAsState()

    Column(Modifier.padding(16.dp)) {
        Text("You’ve been invited as an Admin")
        Spacer(Modifier.height(8.dp))
        TextField(value = password, onValueChange = { password = it }, label = { Text("Set Password") })
        Spacer(Modifier.height(16.dp))
        Button(onClick = { viewModel.acceptInvite(token, password) }) {
            Text("Accept Invite")
        }

        when (state) {
            is AdminUiState.Loading -> Text("Creating account...")
            is AdminUiState.Success -> Text((state as AdminUiState.Success).message)
            is AdminUiState.Error -> Text((state as AdminUiState.Error).message, color = Color.Red)
            else -> {}
        }
    }
}
