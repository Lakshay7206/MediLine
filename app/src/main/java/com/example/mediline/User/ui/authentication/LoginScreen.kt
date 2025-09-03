package com.example.mediline.User.ui.authentication

import android.annotation.SuppressLint
import android.app.Activity
import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.material3.OutlinedTextField

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButtonDefaults.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.mediline.User.ui.theme.AppTheme
import com.example.mediline.User.ui.theme.AppTypography
import com.example.mediline.User.ui.theme.LightColors


@SuppressLint("ContextCastToActivity", "SuspiciousIndentation")
@Composable
fun LoginScreen(
    authViewModel: AuthViewModel,
    onNavigateOtp: (String) -> Unit,
    onNavigateSignUp: ()->Unit
) {
    val uiState by authViewModel.uiState.collectAsState()
    val formState by authViewModel.formState.collectAsState()
    val context = LocalContext.current as Activity

    Scaffold(
        topBar = {CurvedTopBar("Log in",false)}

    )
    { padding->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(LightColors.background)
                .padding(24.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.Center),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {


                Text(
                    text = "Log in",
                    style = AppTypography.displayLarge,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(bottom = 32.dp)
                )
                Text(
                    text = "Enter your phone number to continue",
                    style = AppTypography.bodyMedium,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(bottom = 48.dp)
                )

//                OutlinedTextField(
//                    value = formState.phone,
//                    onValueChange = { authViewModel.updatePhone(it) },
//                    label = { Text("Phone Number",
//                        style = AppTypography.bodyLarge
//                    )
//                            },
//                    modifier = Modifier.fillMaxWidth(),
//                    shape = RoundedCornerShape(20.dp)
//                )
                OutlinedTextField(
                    value = formState.phone,
                    onValueChange = { authViewModel.updatePhone(it) },
                    label = {
                        Text(
                            "Phone Number",
                            style = AppTypography.bodyLarge
                        )
                    }, // Ensure AppTypography styles (especially colors) are M3 compatible
                    textStyle = AppTypography.bodyLarge, // Ensure AppTypography styles are M3 compatible
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(), // The Modifier.background is less idiomatic for M3 TextFields
                    shape = RoundedCornerShape(20.dp), // Apply shape directly to the M3 OutlinedTextField
                    colors = OutlinedTextFieldDefaults.colors(

                        focusedTextColor = Color.Black,
                        unfocusedTextColor = Color.Black, // Often the same unless you want a different unfocused text color

                        focusedContainerColor = LightColors.surface,
                        unfocusedContainerColor = LightColors.surface,

                        cursorColor = LightColors.primary,

                        focusedBorderColor = LightColors.primary,
                        unfocusedBorderColor = LightColors.primary.copy(alpha = 0.5f), // Your existing logic

                        focusedLabelColor = LightColors.primary, // Often good to match the focused border
                        unfocusedLabelColor = LightColors.onSurface.copy(alpha = 0.7f), // A common practice for unfocused labels
                    )
                )


                Spacer(Modifier.height(24.dp))

                Button(
                    onClick = { authViewModel.sendOtp(formState.phone, context) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = LightColors.primary,
                        contentColor = LightColors.onPrimary
                    )
                ) {
                    Text("Send OTP", style = AppTypography.titleLarge)
                }

                Spacer(Modifier.height(16.dp))

// "Don't have an account? Sign up here"
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Don’t have an account? ",
                        style = AppTypography.bodyMedium.copy(color = Color.Gray)
                    )
                    Text(
                        text = "Sign up here",
                        style = AppTypography.bodyMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = LightColors.primary
                        ),
                        modifier = Modifier.clickable {
                            onNavigateSignUp()
                        }
                    )
                }

                when (val state = uiState) {
                    is AuthUiState.Loading -> CircularProgressIndicator(
                        color = LightColors.primary,
                        modifier = Modifier.padding(top = 16.dp)
                    )

                    is AuthUiState.OtpSent -> {
                        LaunchedEffect(Unit) {
                            onNavigateOtp(state.verificationId)
                        }
                    }

                    is AuthUiState.Error -> Text(
                        text = state.message,
                        color = Color.Red,
                        style = AppTypography.bodyLarge,
                        modifier = Modifier.padding(top = 16.dp)
                    )

                    else -> {}
                }
            }
        }
    }

}

@Composable
fun CurvedTopBar(
    title: String,
    enabled: Boolean,
    navigateBack: (() -> Unit)? = null
) {
    val primary = MaterialTheme.colorScheme.primary
    val onPrimary = MaterialTheme.colorScheme.onPrimary

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp),

        ) {
        Canvas(modifier = Modifier.matchParentSize()) {
            val width = size.width
            val height = size.height

            val path = Path().apply {
                moveTo(0f, height * 0.6f)
                quadraticBezierTo(
                    width / 2, height, // control point
                    width, height * 0.6f // end point
                )
                lineTo(width, 0f)
                lineTo(0f, 0f)
                close()
            }

            drawPath(
                path = path,
                color = primary
            )
        }
        if(enabled){
            IconButton(
                onClick = { navigateBack?.invoke() },
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(16.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = onPrimary
                )
            }
        }
        Text(
            text = title,
            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
            color = onPrimary,
            modifier = Modifier
                .align(Alignment.Center)
        )
    }
}

//
//
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
