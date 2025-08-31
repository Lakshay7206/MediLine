package com.example.mediline.User.ui.authentication

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue

import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mediline.data.model.User
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun SignupScreen(
    viewModel: AuthViewModel = hiltViewModel(),
    navigateHome: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val formState by viewModel.formState.collectAsState()

    // Color palette
    val primaryColor = Color(0xFF3BB77E)   // Green
    val textColor = Color(0xFF010F1C)      // Dark
    val hintColor = Color(0xFF646464)      // Gray
    val borderColor = Color(0xFF939393)    // Light gray

    var phoneInput by remember { mutableStateOf(formState.phone) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Title
        Text(
            text = "Sign up",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = textColor
        )
        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Create a new account to get started with MediLine and enjoy seamless healthcare",
            fontSize = 14.sp,
            color = hintColor,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(24.dp))

        // ✅ Phone input with +91 box
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .border(1.dp, borderColor, RoundedCornerShape(8.dp))
                .background(Color.White, RoundedCornerShape(8.dp)),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // +91 Box
            Box(
                modifier = Modifier
                    .background(primaryColor.copy(alpha = 0.1f))
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "+91",
                    color = primaryColor,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            // Phone number field
            OutlinedTextField(
                value = phoneInput.removePrefix("+91"),
                onValueChange = { input ->
                    val digitsOnly = input.filter { it.isDigit() }
                    phoneInput = "+91$digitsOnly"
                    viewModel.updatePhone(phoneInput) // update ViewModel
                },
                placeholder = { Text("Phone number", color = hintColor) },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                modifier = Modifier.weight(1f),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent,
                    cursorColor = primaryColor,
                    focusedTextColor = textColor,
                    unfocusedTextColor = textColor,
                    focusedPlaceholderColor = hintColor,
                    unfocusedPlaceholderColor = hintColor
                )
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // User object
        val user = User(
            id = formState.uid,
            phone = phoneInput,
            createdAt = System.currentTimeMillis()
        )

        // Sign Up Button
        Button(
            onClick = { viewModel.createUser(user) },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = MaterialTheme.shapes.medium,
            colors = ButtonDefaults.buttonColors(containerColor = primaryColor)
        ) {
            Text("Sign up", color = Color.White, fontWeight = FontWeight.Bold)
        }
    }

    Log.d("Logins", formState.phone)

    // State handling
    when (uiState) {
        is AuthUiState.Loading -> CircularProgressIndicator(color = primaryColor)
        is AuthUiState.UserCreated -> {
            LaunchedEffect(Unit) { navigateHome() }
        }
        is AuthUiState.Error -> Text((uiState as AuthUiState.Error).message, color = Color.Red)
        else -> {}
    }
}



//
//
//@Composable
//fun SignupScreen(
//    viewModel: AuthViewModel,
//    navigateHome:()->Unit
//
//
//) {
//    val uiState by viewModel.uiState.collectAsState()
//    val formState by viewModel.formState.collectAsState()
//    Column(Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.Center) {
//        Text(text ="New user")
//        Text(text ="creating User")
//        val user=User(
//            id = formState.uid,
//            phone = formState.phone,
//            createdAt = System.currentTimeMillis()
//        )
//        Button(onClick = { viewModel.createUser(user) }) { Text("Sign up")}
//    }
//
//    Log.d("Logins", formState.phone)
//
//    when (uiState) {
//        is AuthUiState.Loading -> CircularProgressIndicator()
//        is AuthUiState.UserCreated -> {
//            LaunchedEffect(Unit) {
//                navigateHome()
//            }
//        }
//        is AuthUiState.Error -> Text((uiState as AuthUiState.Error).message, color = Color.Red)
//        else -> {}
//    }
//
//
//}
