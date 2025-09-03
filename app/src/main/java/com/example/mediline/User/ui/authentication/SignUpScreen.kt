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
import com.example.mediline.User.ui.theme.AppTypography
import com.example.mediline.User.ui.theme.LightColors
import com.example.mediline.User.ui.theme.PrimaryGreen

@Composable
fun SignupScreen(
    viewModel: AuthViewModel = hiltViewModel(),
    navigateHome: () -> Unit,
    navigateBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val formState by viewModel.formState.collectAsState()

    var phoneInput by remember { mutableStateOf(formState.phone) }

    Scaffold(
        topBar = {
            CurvedTopBar("Sign up", true, navigateBack)
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Title

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Create a new account to get started with MediLine and enjoy seamless healthcare",
                fontSize = 14.sp,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(24.dp))

            // ✅ Phone input with +91 box
            Column (
                modifier = Modifier
                    .fillMaxWidth()

            ) {
                // +91 Box
                OutlinedTextField(
                    value = formState.otp,
                    onValueChange = { viewModel.updateOtp(it) },
                    label = {
                        Text(
                            "Phone Number",
                            style = AppTypography.bodyLarge
                        )
                    }, // Ensure AppTypography styles (especially colors) are M3 compatible
                    textStyle = AppTypography.bodyLarge, // Ensure AppTypography styles are M3 compatible
                    singleLine = true,
                     modifier = Modifier.fillMaxWidth(),// The Modifier.background is less idiomatic for M3 TextFields
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
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = LightColors.primary,
                        contentColor = LightColors.onPrimary
                    )
                ) {
                    Text("Sign Up", style = AppTypography.titleLarge)
                }
            }
        }


    Log.d("Logins", formState.phone)

    // State handling
    when (uiState) {
        is AuthUiState.Loading -> CircularProgressIndicator(color = PrimaryGreen)
        is AuthUiState.UserCreated -> {
            LaunchedEffect(Unit) { navigateHome() }
        }

        is AuthUiState.Error -> Text(
            (uiState as AuthUiState.Error).message,
            color = Color.Red
        )

        else -> {}
    }
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
