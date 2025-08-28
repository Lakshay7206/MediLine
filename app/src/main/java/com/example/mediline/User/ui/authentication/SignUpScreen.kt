package com.example.mediline.User.ui.authentication

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.mediline.data.model.User

@Composable
fun SignupScreen(
    viewModel: AuthViewModel,
    navigateHome:()->Unit


) {
    val uiState by viewModel.uiState.collectAsState()
    val formState by viewModel.formState.collectAsState()
    Column(Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.Center) {
        Text(text ="New user")
        Text(text ="creating User")
        val user=User(
            id = formState.uid,
            phone = formState.phone,
            createdAt = System.currentTimeMillis()
        )
        Button(onClick = { viewModel.createUser(user) }) { Text("Sign up")}
    }

    Log.d("Logins", formState.phone)

    when (uiState) {
        is AuthUiState.Loading -> CircularProgressIndicator()
        is AuthUiState.UserCreated -> {
            LaunchedEffect(Unit) {
                navigateHome()
            }
        }
        is AuthUiState.Error -> Text((uiState as AuthUiState.Error).message, color = Color.Red)
        else -> {}
    }


}
