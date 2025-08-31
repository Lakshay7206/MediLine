package com.example.mediline.User.ui.authentication

import android.util.Log
import androidx.compose.ui.input.key.Key

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@Composable
fun OtpScreen(
    viewModel: AuthViewModel= hiltViewModel(),
    onUserExists: () -> Unit,
    onNewUser: () -> Unit,
    onResendOtp: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val formState by viewModel.formState.collectAsState()

    val primaryColor = Color(0xFF3BB77E)
    val textColor = Color(0xFF010F1C)
    val hintColor = Color(0xFF646464)
    val borderColor = Color(0xFF939393)

    var otpDigits by remember { mutableStateOf(List(6) { "" }) }
    val focusRequesters = List(6) { FocusRequester() }

    // ✅ OTP state
    val otp = otpDigits.joinToString("")
    LaunchedEffect(otp) { viewModel.updateOtp(otp) }

    // ✅ Resend OTP Timer
    var timeLeft by remember { mutableStateOf(30) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        while (timeLeft > 0) {
            delay(1000L)
            timeLeft--
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Enter OTP", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = textColor)
        Spacer(Modifier.height(24.dp))

        // ✅ OTP Input Boxes
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            otpDigits.forEachIndexed { index, digit ->
                BasicTextField(
                    value = digit,
                    onValueChange = { newValue ->
                        if (newValue.length <= 1 && newValue.all { it.isDigit() }) {
                            val newDigits = otpDigits.toMutableList()
                            newDigits[index] = newValue
                            otpDigits = newDigits

                            if (newValue.isNotEmpty() && index < 5) {
                                focusRequesters[index + 1].requestFocus()
                            }
                        }
                    },
                    textStyle = TextStyle(
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = textColor,
                        textAlign = TextAlign.Center
                    ),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier
                        .width(48.dp)
                        .height(56.dp)
                        .border(
                            2.dp,
                            if (digit.isNotEmpty()) primaryColor else borderColor,
                            shape = RoundedCornerShape(8.dp)
                        )
                        .focusRequester(focusRequesters[index])
                        .onKeyEvent { keyEvent ->
                            if (keyEvent.type == KeyEventType.KeyDown && keyEvent.key == Key.Backspace) {
                                val newDigits = otpDigits.toMutableList()
                                if (digit.isNotEmpty()) {
                                    newDigits[index] = ""
                                    otpDigits = newDigits
                                } else if (index > 0) {
                                    newDigits[index - 1] = ""
                                    otpDigits = newDigits
                                    focusRequesters[index - 1].requestFocus()
                                }
                                true
                            } else false
                        },
                    decorationBox = { innerTextField ->
                        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                            if (digit.isEmpty()) {
                                Text("•", color = hintColor, fontSize = 20.sp)
                            }
                            innerTextField()
                        }
                    }
                )
            }
        }

        Spacer(Modifier.height(32.dp))

        // ✅ Verify OTP Button
        Button(
            onClick = {
                viewModel.verifyOtp(
                    verificationId = formState.verificationId,
                    otp = formState.otp
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = primaryColor),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text("Verify OTP", color = Color.White, fontWeight = FontWeight.Bold)
        }

        Spacer(Modifier.height(16.dp))

        // ✅ Resend OTP Section
        if (timeLeft > 0) {
            Text(
                text = "Resend OTP in ${timeLeft}s",
                color = hintColor,
                fontSize = 14.sp,
                modifier = Modifier.padding(top = 8.dp)
            )
        } else {
            TextButton(onClick = {
                timeLeft = 30
                scope.launch {
                    while (timeLeft > 0) {
                        delay(1000L)
                        timeLeft--
                    }
                }
                onResendOtp()
            }) {
                Text("Resend OTP", color = primaryColor, fontWeight = FontWeight.Bold)
            }
        }

        Spacer(Modifier.height(16.dp))

        when (uiState) {
            is AuthUiState.Loading -> CircularProgressIndicator(color = primaryColor)
            is AuthUiState.UserExists -> LaunchedEffect(Unit) { onUserExists() }
            is AuthUiState.NewUser -> LaunchedEffect(Unit) { onNewUser() }
            is AuthUiState.Error -> Text("OTP verification failed", color = Color.Red)
            else -> {}
        }
    }

    // ✅ Autofocus first box
    LaunchedEffect(Unit) {
        focusRequesters[0].requestFocus()
    }
}












//@Composable
//fun OtpScreen(
//    viewModel: AuthViewModel= hiltViewModel(),
//    onUserExists: () -> Unit,
//    onNewUser: () -> Unit
//) {
//    val uiState by viewModel.uiState.collectAsState()
//    val formState by viewModel.formState.collectAsState()
//
//    val primaryColor = Color(0xFF3BB77E)
//    val textColor = Color(0xFF010F1C)
//    val hintColor = Color(0xFF646464)
//    val borderColor = Color(0xFF939393)
//
//    var otpDigits by remember { mutableStateOf(List(6) { "" }) }
//    val focusRequesters = List(6) { FocusRequester() }
//
//    val otp = otpDigits.joinToString("")
//    LaunchedEffect(otp) { viewModel.updateOtp(otp) }
//
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .padding(24.dp),
//        verticalArrangement = Arrangement.Center,
//        horizontalAlignment = Alignment.CenterHorizontally
//    ) {
//        Text("Enter OTP", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = textColor)
//        Spacer(Modifier.height(24.dp))
//
//        Row(
//            modifier = Modifier.fillMaxWidth(),
//            horizontalArrangement = Arrangement.SpaceEvenly
//        ) {
//            otpDigits.forEachIndexed { index, digit ->
//                BasicTextField(
//                    value = digit,
//                    onValueChange = { newValue ->
//                        if (newValue.length <= 1 && newValue.all { it.isDigit() }) {
//                            val newDigits = otpDigits.toMutableList()
//                            newDigits[index] = newValue
//                            otpDigits = newDigits
//
//                            // ✅ Move forward on typing
//                            if (newValue.isNotEmpty() && index < 5) {
//                                focusRequesters[index + 1].requestFocus()
//                            }
//                        }
//                    },
//                    textStyle = TextStyle(
//                        fontSize = 22.sp,
//                        fontWeight = FontWeight.Bold,
//                        color = textColor,
//                        textAlign = TextAlign.Center
//                    ),
//                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
//                    modifier = Modifier
//                        .width(48.dp)
//                        .height(56.dp)
//                        .border(
//                            2.dp,
//                            if (digit.isNotEmpty()) primaryColor else borderColor,
//                            shape = RoundedCornerShape(8.dp)
//                        )
//                        .focusRequester(focusRequesters[index])
//                        .onKeyEvent { keyEvent ->
//                            if (keyEvent.type == KeyEventType.KeyDown && keyEvent.key == Key.Backspace) {
//                                val newDigits = otpDigits.toMutableList()
//                                if (digit.isNotEmpty()) {
//                                    // ✅ First backspace clears current box
//                                    newDigits[index] = ""
//                                    otpDigits = newDigits
//                                } else if (index > 0) {
//                                    // ✅ Second backspace jumps to previous
//                                    newDigits[index - 1] = ""
//                                    otpDigits = newDigits
//                                    focusRequesters[index - 1].requestFocus()
//                                }
//                                true
//                            } else {
//                                false
//                            }
//                        },
//                    decorationBox = { innerTextField ->
//                        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
//                            if (digit.isEmpty()) {
//                                Text("•", color = hintColor, fontSize = 20.sp)
//                            }
//                            innerTextField()
//                        }
//                    }
//                )
//            }
//        }
//
//        Spacer(Modifier.height(32.dp))
//
//        Button(
//            onClick = {
//                viewModel.verifyOtp(
//                    verificationId = formState.verificationId,
//                    otp = formState.otp
//                )
//            },
//            modifier = Modifier
//                .fillMaxWidth()
//                .height(50.dp),
//            colors = ButtonDefaults.buttonColors(containerColor = primaryColor),
//            shape = RoundedCornerShape(8.dp)
//        ) {
//            Text("Verify OTP", color = Color.White, fontWeight = FontWeight.Bold)
//        }
//
//        Spacer(Modifier.height(16.dp))
//
//        when (uiState) {
//            is AuthUiState.Loading -> CircularProgressIndicator(color = primaryColor)
//            is AuthUiState.UserExists -> LaunchedEffect(Unit) { onUserExists() }
//            is AuthUiState.NewUser -> LaunchedEffect(Unit) { onNewUser() }
//            is AuthUiState.Error -> Text("OTP verification failed", color = Color.Red)
//            else -> {}
//        }
//    }
//
//    // ✅ Autofocus first box on launch
//    LaunchedEffect(Unit) {
//        focusRequesters[0].requestFocus()
//    }
//}













//@Composable
//fun OtpScreen(
//    viewModel: AuthViewModel,
//    onUserExists: () -> Unit,
//    onNewUser: () -> Unit
//
//) {
//    val uiStateState = viewModel.uiState.collectAsState()
//    val uiState = uiStateState.value
//     val formState by viewModel.formState.collectAsState()
//
//
//    Column(Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.Center) {
//        OutlinedTextField(
//            value = formState.otp,
//            onValueChange = { viewModel.updateOtp(it) },
//            label = { Text("Enter OTP") },
//            modifier = Modifier.fillMaxWidth()
//        )
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
