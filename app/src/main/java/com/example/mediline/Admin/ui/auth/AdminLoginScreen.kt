package com.example.mediline.Admin.ui.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel



@Composable
fun AdminLoginScreen(
    onLoginSuccess: () -> Unit,
    viewModel: AdminLoginViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    val primaryColor = Color(0xFF3BB77E)
    val textColor = Color(0xFF010F1C)
    val hintColor = Color(0xFF646464)
    val borderColor = Color(0xFF939393)

    // ✅ Password visibility state
    var passwordVisible by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            "Admin Login",
            style = MaterialTheme.typography.headlineSmall.copy(
                fontWeight = FontWeight.Bold,
                color = textColor
            )
        )

        Spacer(modifier = Modifier.height(32.dp))

        // ✅ Email Field
        OutlinedTextField(
            value = uiState.email,
            onValueChange = { viewModel.onEmailChange(it) },
            label = { Text("Email", color = hintColor) },
            singleLine = true,
            textStyle = TextStyle(color = Color(0xFF010F1C)),
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = primaryColor,
                unfocusedBorderColor = borderColor,
                focusedLabelColor = primaryColor,
                unfocusedLabelColor = hintColor,
                cursorColor = primaryColor
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        // ✅ Password Field with Eye Icon
        OutlinedTextField(
            value = uiState.password,
            onValueChange = { viewModel.onPasswordChange(it) },
            label = { Text("Password", color = hintColor) },
            singleLine = true,
            textStyle = TextStyle(color = Color(0xFF010F1C)),
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            trailingIcon = {
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(
                        imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                        contentDescription = if (passwordVisible) "Hide password" else "Show password",
                        tint = if (passwordVisible) primaryColor else hintColor
                    )
                }
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = primaryColor,
                unfocusedBorderColor = borderColor,
                focusedLabelColor = primaryColor,
                unfocusedLabelColor = hintColor,
                cursorColor = primaryColor
            )
        )

        Spacer(modifier = Modifier.height(24.dp))

        // ✅ Login Button
        Button(
            onClick = { viewModel.loginAdmin(onLoginSuccess) },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = primaryColor),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text("Login", color = Color.White, fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // ✅ Error Message
        if (uiState.error.isNotEmpty()) {
            Text(uiState.error, color = Color.Red, fontSize = 14.sp)
        }
    }
}








//@Composable
//fun AdminLoginScreen(
//    onLoginSuccess: () -> Unit,
//    viewModel: AdminLoginViewModel = hiltViewModel()
//) {
//    val uiState by viewModel.uiState.collectAsState()
//
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .padding(16.dp),
//        verticalArrangement = Arrangement.Center
//    ) {
//        Text("Admin Login", style = MaterialTheme.typography.bodyMedium)
//
//        Spacer(modifier = Modifier.height(16.dp))
//
//        TextField(
//            value = uiState.email,
//            onValueChange = { viewModel.onEmailChange(it) },
//            label = { Text("Email") }
//        )
//
//        Spacer(modifier = Modifier.height(8.dp))
//
//        TextField(
//            value = uiState.password,
//            onValueChange = { viewModel.onPasswordChange(it) },
//            label = { Text("Password") },
//            visualTransformation = PasswordVisualTransformation()
//        )
//
//        Spacer(modifier = Modifier.height(16.dp))
//
//        Button(onClick = { viewModel.loginAdmin(onLoginSuccess) }) {
//            Text("Login")
//        }
//
//        if (uiState.error.isNotEmpty()) {
//            Text(uiState.error, color = Color.Red)
//        }
//    }
//}
