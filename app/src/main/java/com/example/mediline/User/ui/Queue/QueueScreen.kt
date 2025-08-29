package com.example.mediline.User.ui.Queue


import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun QueueScreen(
    departmentId: String,
    navigateToCreateTicket: (String) -> Unit,
    navigateToViewTicket: (String) -> Unit,
    viewModel: QueueViewModel = hiltViewModel()
) {
    Log.d("QueueScreen", "departmentId: $departmentId")
    val queueState by viewModel.queueState.collectAsState()

    LaunchedEffect(departmentId) {
        viewModel.observeQueue(departmentId)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        when (queueState) {
            is QueueUiState.Loading -> {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
            }

            is QueueUiState.Error -> {
                val error = (queueState as QueueUiState.Error).message
                Text(
                    text = "Error: $error",
                    color = Color.Red,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }

            is QueueUiState.Success -> {
                val queueNumber = (queueState as QueueUiState.Success).queueNumber

                // Current Number Card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFE3F2FD)),
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("Now Serving", fontSize = 18.sp, fontWeight = FontWeight.Medium)
                        Text(
                            "$queueNumber",
                            fontSize = 48.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1976D2)
                        )
                        Text("Estimated wait: ~${queueNumber * 2} min", fontSize = 14.sp, color = Color.Gray) // simple estimate
                        LinearProgressIndicator(
                            progress = (queueNumber % 10) / 10f,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 8.dp)
                        )
                    }
                }

                Spacer(Modifier.height(24.dp))

                // Total Patients Card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFE3F2FD)),
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("Total patients", fontSize = 18.sp, fontWeight = FontWeight.Medium)
                        Text(
                            "$queueNumber", // Example: total > current
                            fontSize = 48.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1976D2)
                        )
                    }
                }

                Spacer(Modifier.height(24.dp))

                // Quick Actions
                Button(
                    onClick = { navigateToCreateTicket(departmentId) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Book Appointment")
                }

                OutlinedButton(
                    onClick ={ navigateToViewTicket(departmentId) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("View My Tickets")
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun QueueScreenPreview() {
    QueueScreen("1", {}, {})
}
