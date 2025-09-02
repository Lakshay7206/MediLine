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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController


@SuppressLint("ContextCastToActivity")
@Composable
fun OtpScreen(
    viewModel: AuthViewModel,
    onUserExists: () -> Unit,
    onNewUser: () -> Unit

) {
    val uiStateState = viewModel.uiState.collectAsState()
    val uiState = uiStateState.value
     val formState by viewModel.formState.collectAsState()
    val context = LocalContext.current as Activity


    Column(Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.Center) {
        OutlinedTextField(
            value = formState.otp,
            onValueChange = { viewModel.updateOtp(it) },
            label = { Text("Enter OTP") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(16.dp))
        Log.d("Logino", formState.phone)

        Button(
            onClick = { viewModel.verifyOtp(
                verificationId = formState.verificationId,
                otp = formState.otp
            ) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Verify OTP")
        }
        Button(
                onClick = { viewModel.resendOtp(activity = context ) },
        modifier = Modifier.fillMaxWidth()
        ) {
        Text("Verify OTP")
    }


        when (uiState) {
            is AuthUiState.Loading -> CircularProgressIndicator()
            is AuthUiState.UserExists -> {
                LaunchedEffect(Unit) { onUserExists() }
            }
            is AuthUiState.NewUser -> {
                LaunchedEffect(Unit) { onNewUser() }
            }
            is AuthUiState.Error -> Text(uiState.message, color = Color.Red)
            else -> {}
        }
    }
}
