package com.example.mediline.User.ui.authentication

import android.annotation.SuppressLint
import android.app.Activity
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.SegmentedButtonDefaults.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
















@SuppressLint("ContextCastToActivity")
@Composable
fun LoginScreen(
    onNavigateOtp: (String) -> Unit,
    authViewModel: AuthViewModel= hiltViewModel(),
) {
    val uiState by authViewModel.uiState.collectAsState()
    val formState by authViewModel.formState.collectAsState()
    val context = LocalContext.current as Activity

    // Background White
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // Back button
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFF5F5F5)),
                contentAlignment = Alignment.Center
            ) {
                androidx.compose.material3.Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = Color(0xFF010F1C)
                )
            }

            Spacer(Modifier.height(16.dp))

            // Title
            Text(
                text = "Log in",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF010F1C)
            )
            Text(
                text = "Enter your phone number to securely access your account and manage your services.",
                fontSize = 14.sp,
                color = Color(0xFF646464),
                modifier = Modifier.padding(top = 4.dp),
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(32.dp))

//            // Phone field

//            var phoneInput by remember { mutableStateOf("+91") }
//
//            OutlinedTextField(
//                value = phoneInput,
//                onValueChange = { input ->
//                    // If user deletes everything, allow empty string
//                    if (input.isEmpty()) {
//                        phoneInput = ""
//                        authViewModel.updatePhone(phoneInput)
//                    } else {
//                        // Allow + and digits only
//                        if (input.all { it.isDigit() || it == '+' }) {
//                            phoneInput = input
//                            authViewModel.updatePhone(phoneInput)
//                        }
//                    }
//                },
//                placeholder = { Text("Phone number", color = Color(0xFF939393)) },
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .clip(RoundedCornerShape(50.dp)),
//                singleLine = true,
//                textStyle = TextStyle(color = Color(0xFF010F1C)), // black numbers
//                keyboardOptions = KeyboardOptions(
//                    keyboardType = KeyboardType.Phone, // number + '+'
//                    imeAction = ImeAction.Done
//                ),
//                colors = OutlinedTextFieldDefaults.colors(
//                    focusedBorderColor = Color(0xFF3BB77E),
//                    unfocusedBorderColor = Color(0xFF939393),
//                    cursorColor = Color(0xFF010F1C)
//                )
//            )
//
//// Auto-restore +91 when field is completely empty
//            LaunchedEffect(phoneInput) {
//                if (phoneInput.isEmpty()) {
//                    phoneInput = "+91"
//                    authViewModel.updatePhone(phoneInput)
//                }
//            }






// Ensure +91 prefix is always there
            var phoneInput by remember { mutableStateOf("") }

            val fullNumber = if (phoneInput.startsWith("+91")) phoneInput else "+91${phoneInput.removePrefix("+91")}"

            OutlinedTextField(
                value = phoneInput,
                onValueChange = { input ->
                    // Allow only digits (excluding +)
                    val digitsOnly = input.filter { it.isDigit() }
                    phoneInput = digitsOnly
                    authViewModel.updatePhone("+91$digitsOnly")
                },
                label = { Text("Phone number") },
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(50.dp)),
                singleLine = true,
                textStyle = TextStyle(color = Color(0xFF010F1C)), // black input
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Phone,
                    imeAction = ImeAction.Done
                ),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF3BB77E),
                    unfocusedBorderColor = Color(0xFF939393),
                    cursorColor = Color(0xFF010F1C)
                ),
                prefix = {
                    Text(
                        text = "+91 ",
                        color = Color(0xFF010F1C),
                        fontWeight = FontWeight.Bold
                    )
                }
            )


//            OutlinedTextField(
//                value = formState.phone,
//                onValueChange = { input ->
//                    // Allow only digits and '+' at the beginning
//                    if (input.all { it.isDigit() || it == '+' }) {
//                        authViewModel.updatePhone(input)
//                    }
//                },
//                placeholder = { Text("Phone number", color = Color(0xFF939393)) },
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .clip(RoundedCornerShape(50.dp)),
//                singleLine = true,
//                textStyle = TextStyle(color = Color(0xFF010F1C)), // entered number black
//                keyboardOptions = KeyboardOptions(
//                    keyboardType = KeyboardType.Phone, // shows + on most keyboards
//                    imeAction = ImeAction.Done
//                ),
//                colors = OutlinedTextFieldDefaults.colors(
//                    focusedBorderColor = Color(0xFF3BB77E),
//                    unfocusedBorderColor = Color(0xFF939393),
//                    cursorColor = Color(0xFF010F1C)
//                )
//            )



            Spacer(Modifier.height(20.dp))

            // Login button
            Button(
                onClick = { authViewModel.sendOtp(formState.phone, context) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .clip(RoundedCornerShape(50.dp)),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF3BB77E),
                    contentColor = Color.White
                )
            ) {
                Text("Login", fontWeight = FontWeight.Bold)
            }

            Spacer(Modifier.height(16.dp))

            // Sign up link
            Text(
                buildAnnotatedString {
                    append("Don’t have an account? ")
                    withStyle(style = SpanStyle(
                        color = Color(0xFF3BB77E),
                        fontWeight = FontWeight.Bold
                    )
                    ) {
                        append("Sign Up here")
                    }
                },
                fontSize = 14.sp,
                color = Color(0xFF646464)
            )

            Spacer(Modifier.height(20.dp))

            // State handling
            when (val state = uiState) {
                is AuthUiState.Loading -> CircularProgressIndicator(color = Color(0xFF3BB77E))
                is AuthUiState.OtpSent -> {
                    LaunchedEffect(Unit) {
                        onNavigateOtp(state.verificationId)
                    }
                }
                is AuthUiState.Error -> Text(
                    state.message,
                    color = Color.Red,
                    fontSize = 14.sp
                )
                else -> {}
            }
        }
    }
}
















//@SuppressLint("ContextCastToActivity")
//@Composable
//fun LoginScreen(
//    authViewModel: AuthViewModel,
//    onNavigateOtp: (String) -> Unit
//
//) {
//    val uiState by authViewModel.uiState.collectAsState()
//    val context = LocalContext.current as Activity
//    val formState by authViewModel.formState.collectAsState()
//
//    Column(Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.Center) {
//        OutlinedTextField(
//            value = formState.phone,
//            onValueChange = { authViewModel.updatePhone(it) },
//            label = { Text("Enter phone number") },
//            modifier = Modifier.fillMaxWidth()
//        )
//
//        Spacer(Modifier.height(16.dp))
//        Log.d("Loginl", formState.phone)
//
//        Button(
//            onClick = { authViewModel.sendOtp(formState.phone, context) },
//            modifier = Modifier.fillMaxWidth()
//        ) {
//            Text("Send OTP")
//        }
//        val state=uiState
//        when (state) {
//            is AuthUiState.Loading -> CircularProgressIndicator()
//            is AuthUiState.OtpSent -> {
//                LaunchedEffect(Unit) {
//                    onNavigateOtp(state.verificationId)
//                }
//            }
//            is AuthUiState.Error -> Text(state.message, color = Color.Red)
//            else -> {}
//        }
//    }
//}
