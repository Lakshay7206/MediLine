package com.example.mediline.User.ui.Queue


import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
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
import com.google.accompanist.flowlayout.FlowRow

@Composable
fun QueueScreen(
    departmentId: String,
    navigateToCreateTicket: () -> Unit,
    navigateToViewTicket: () -> Unit,
    viewModel: QueueViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    LaunchedEffect(departmentId) {
        viewModel.loadTodaysTickets(departmentId)
    }

    when (uiState) {
        is QueueUiState.Loading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
        is QueueUiState.Error -> {
            val message = (uiState as QueueUiState.Error).message
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(message, color = Color.Red, fontWeight = FontWeight.Bold)
                Log.d("QueueScreen", "Error: $message")
            }
        }
        is QueueUiState.Success -> {
            val state = uiState as QueueUiState.Success
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                // --- Top Summary Cards ---
                Column(
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    BigSummaryCard("Now Treating", state.currentNumber, Color(0xFFFFC107))
                    BigSummaryCard("Total Patients", state.totalPatients, Color(0xFF64B5F6))
                    BigSummaryCard(
                        "Patients Ahead of You",
                        (state.totalPatients - state.currentNumber).coerceAtLeast(0),
                        Color(0xFF81C784)
                    )
                }

                // --- Queue Grid ---
                FlowRow(
                    mainAxisSpacing = 6.dp,
                    crossAxisSpacing = 6.dp,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    state.ticketBoxes.forEach { box ->
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .size(40.dp)
                                .background(box.color, RoundedCornerShape(8.dp))
                        ) {
                            Text(
                                "${box.ticketNumber}",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }
                }

                // --- Quick Actions ---
                Column(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Button(
                        onClick = navigateToCreateTicket,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Text("Book Appointment", fontSize = 16.sp)
                    }

                    OutlinedButton(
                        onClick = navigateToViewTicket,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Text("View My Tickets", fontSize = 16.sp)
                    }
                }
            }
        }
    }
}


//@Composable
//fun QueueScreen(
//    navigateToCreateTicket: () -> Unit,
//    navigateToViewTicket: () -> Unit,
//    viewModel: QueueViewModel=hiltViewModel()
//) {
//    val uistate by viewModel.uiState.collectAsState()
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .padding(16.dp),
//        verticalArrangement = Arrangement.spacedBy(24.dp)
//    ) {
//        // --- Top Summary Cards ---
//        Column(
//            verticalArrangement = Arrangement.spacedBy(16.dp),
//            modifier = Modifier.fillMaxWidth()
//        ) {
//            BigSummaryCard("Now Treating", currentNumber, Color(0xFFFFC107))
//            BigSummaryCard("Total Patients", totalPatients, Color(0xFF64B5F6))
//            BigSummaryCard(
//                "Patients Ahead of You",
//                (totalPatients - currentNumber).coerceAtLeast(0),
//                Color(0xFF81C784)
//            )
//        }
//
//
//        // --- Queue Grid ---
//        FlowRow(
//            mainAxisSpacing = 6.dp,
//            crossAxisSpacing = 6.dp,
//            modifier = Modifier.fillMaxWidth()
//        ) {
//            for (i in 1..totalPatients) {
//                val color = when {
//                    i < currentNumber -> Color(0xFF81C784)  // treated
//                    i == currentNumber -> Color(0xFFFFC107) // currently treating
//                    else -> Color(0xFF64B5F6)               // waiting
//                }
//
//                Box(
//                    contentAlignment = Alignment.Center,
//                    modifier = Modifier
//                        .size(40.dp)
//                        .background(color, RoundedCornerShape(8.dp))
//                ) {
//                    Text(
//                        "$i",
//                        fontSize = 14.sp,
//                        fontWeight = FontWeight.Bold,
//                        color = Color.White
//                    )
//                }
//            }
//        }
//        // --- Quick Actions ---
//        Column(
//            verticalArrangement = Arrangement.spacedBy(12.dp),
//            modifier = Modifier.fillMaxWidth()
//        ) {
//            Button(
//                onClick = navigateToCreateTicket,
//                modifier = Modifier.fillMaxWidth(),
//                shape = RoundedCornerShape(16.dp)
//            ) {
//                Text("Book Appointment", fontSize = 16.sp)
//            }
//
//            OutlinedButton(
//                onClick = navigateToViewTicket,
//                modifier = Modifier.fillMaxWidth(),
//                shape = RoundedCornerShape(16.dp)
//            ) {
//                Text("View My Tickets", fontSize = 16.sp)
//            }
//        }
//
//    }
//
//}
//
@Composable
fun BigSummaryCard(title: String, value: Int, color: Color) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp),
        colors = CardDefaults.cardColors(containerColor = color),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Text(title, fontSize = 16.sp, color = Color.White)
            Text(value.toString(), fontSize = 28.sp, fontWeight = FontWeight.Bold, color = Color.White)
        }
    }
}

//@Preview
//@Composable
//fun PreviewQueueScreen() {
//    QueueScreen(
//
//        navigateToCreateTicket = {},
//        navigateToViewTicket = {}
//    )
//}
