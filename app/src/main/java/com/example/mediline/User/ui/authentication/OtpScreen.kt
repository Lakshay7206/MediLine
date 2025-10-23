package com.example.mediline.User.ui.authentication

import android.annotation.SuppressLint
import android.app.Activity
import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.mediline.R
import com.example.mediline.User.ui.theme.AppTypography
import com.example.mediline.User.ui.theme.LightColors
@SuppressLint("ContextCastToActivity")
@Composable
fun OtpScreen(
    viewModel: AuthViewModel,
    onUserExists: () -> Unit,
    onNewUser: () -> Unit,
    navigateBack: () -> Unit

) {
    val uiStateState = viewModel.uiState.collectAsState()
    val uiState = uiStateState.value
    val formState by viewModel.formState.collectAsState()
    val activity = LocalContext.current as Activity

    Scaffold(
        topBar = {
            CurvedTopBar("Enter OTP", true, { navigateBack() })
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(20.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo), // your logo file name
                contentDescription = "App Logo",
                modifier = Modifier
                    .size(120.dp) // adjust size as needed
                    .padding(bottom = 24.dp)
            )
            Text(
                text = "Enter the 6-digit code sent to your number",
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(32.dp))

            // 🔑 OTP TextField Boxes
            OtpInput(
                otp = formState.otp,
                onOtpChange = { viewModel.updateOtp(it) }
            )

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = {
                    viewModel.verifyOtp(
                        verificationId = formState.verificationId,
                        otp = formState.otp
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Verify OTP")
            }

            Spacer(modifier = Modifier.height(12.dp))

            TextButton(
                onClick = { viewModel.resendOtp(activity) },
                modifier = Modifier.fillMaxWidth(),
                enabled = formState.cooldownSeconds == 0 // ✅ disable button during cooldown
            ) {
                if (formState.cooldownSeconds > 0) {
                    Text(
                        "Resend in ${formState.cooldownSeconds}s",
                        color = Color.Gray
                    )
                } else {
                    Text("Resend OTP", color = MaterialTheme.colorScheme.primary)
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            when (uiState) {
                is AuthUiState.Loading -> CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
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
}

@Composable
fun OtpInput(
    otp: String,
    onOtpChange: (String) -> Unit
) {
    val focusRequester = remember { FocusRequester() }

    BasicTextField(
        value = otp,
        onValueChange = {
            if (it.length <= 6 && it.all { ch -> ch.isDigit() }) {
                onOtpChange(it)
            }
        },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
        singleLine = true,
        cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
        decorationBox = {
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                repeat(6) { index ->
                    val char = otp.getOrNull(index)?.toString() ?: ""
                    Box(
                        modifier = Modifier
                            .size(43.dp)
                            .border(
                                BorderStroke(2.dp, if (index == otp.length) MaterialTheme.colorScheme.primary else Color.Gray.copy(alpha = 0.4f)),
                                shape = RoundedCornerShape(8.dp)
                            )
                            .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(8.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = char,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        },
        modifier = Modifier.focusRequester(focusRequester)
    )

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }
}


//@SuppressLint("ContextCastToActivity")
//@Composable
//fun OtpScreen(
//    viewModel: AuthViewModel,
//    onUserExists: () -> Unit,
//    onNewUser: () -> Unit,
//    navigateBack:()->Unit
//
//) {
//    val uiStateState = viewModel.uiState.collectAsState()
//    val uiState = uiStateState.value
//     val formState by viewModel.formState.collectAsState()
//    val context = LocalContext.current as Activity
//
//
//    Column(Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.Center) {
//        CurvedTopBar("Enter OTP ",false,navigateBack)
////        OutlinedTextField(
////            value = formState.otp,
////            onValueChange = { viewModel.updateOtp(it) },
////            label = { Text("Enter OTP") },
////            modifier = Modifier.fillMaxWidth()
////        )
//        OutlinedTextField(
//            value = formState.otp,
//            onValueChange = { viewModel.updateOtp(it) },
//            label = {
//                Text(
//                    "Enter OTP",
//                    style = AppTypography.bodyLarge
//                )
//            }, // Ensure AppTypography styles (especially colors) are M3 compatible
//            textStyle = AppTypography.bodyLarge, // Ensure AppTypography styles are M3 compatible
//            singleLine = true,
//            modifier = Modifier.fillMaxWidth(), // The Modifier.background is less idiomatic for M3 TextFields
//            shape = RoundedCornerShape(20.dp), // Apply shape directly to the M3 OutlinedTextField
//            colors = OutlinedTextFieldDefaults.colors(
//
//                focusedTextColor = Color.Black,
//                unfocusedTextColor = Color.Black, // Often the same unless you want a different unfocused text color
//
//                focusedContainerColor = LightColors.surface,
//                unfocusedContainerColor = LightColors.surface,
//
//                cursorColor = LightColors.primary,
//
//                focusedBorderColor = LightColors.primary,
//                unfocusedBorderColor = LightColors.primary.copy(alpha = 0.5f), // Your existing logic
//
//                focusedLabelColor = LightColors.primary, // Often good to match the focused border
//                unfocusedLabelColor = LightColors.onSurface.copy(alpha = 0.7f), // A common practice for unfocused labels
//            )
//        )
//
//
//        Spacer(Modifier.height(16.dp))
//        Log.d("Logino", formState.phone)
//
//        Button(
//            onClick = { viewModel.verifyOtp(
//                verificationId = formState.verificationId,
//                otp = formState.otp
//            ) },
//            modifier = Modifier.fillMaxWidth()
//        ) {
//            Text("Verify OTP")
//        }
//        Button(
//                onClick = { viewModel.resendOtp(activity = context ) },
//        modifier = Modifier.fillMaxWidth()
//        ) {
//        Text("Verify OTP")
//    }
//
//
//        when (uiState) {
//            is AuthUiState.Loading -> CircularProgressIndicator()
//            is AuthUiState.UserExists -> {
//                LaunchedEffect(Unit) { onUserExists() }
//            }
//            is AuthUiState.NewUser -> {
//                LaunchedEffect(Unit) { onNewUser() }
//            }
//            is AuthUiState.Error -> Text(uiState.message, color = Color.Red)
//            else -> {}
//        }
//    }
//}
