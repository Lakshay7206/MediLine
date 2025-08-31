package com.example.mediline.User.ui.authentication

import android.annotation.SuppressLint
import android.app.Activity
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel




@SuppressLint("ContextCastToActivity")
@Composable
fun LoginScreen(
    authViewModel: AuthViewModel,
    onNavigateOtp: (String) -> Unit

) {
    val uiState by authViewModel.uiState.collectAsState()
    val context = LocalContext.current as Activity
    val formState by authViewModel.formState.collectAsState()

    Column(Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.Center) {
        OutlinedTextField(
            value = formState.phone,
            onValueChange = { authViewModel.updatePhone(it) },
            label = { Text("Enter phone number") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(16.dp))
        Log.d("Loginl", formState.phone)

        Button(
            onClick = { authViewModel.sendOtp(formState.phone, context) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Send OTP")
        }
        val state=uiState
        when (state) {
            is AuthUiState.Loading -> CircularProgressIndicator()
            is AuthUiState.OtpSent -> {
                LaunchedEffect(Unit) {
                    onNavigateOtp(state.verificationId)
                }
            }
            is AuthUiState.Error -> Text(state.message, color = Color.Red)
            else -> {}
        }
    }
}
