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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ReceiptLong
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.mediline.Admin.ui.AdminCreateTicket.CurvedTopBar
import com.example.mediline.User.ui.authentication.CurvedTopBar
import com.google.accompanist.flowlayout.FlowRow

@Composable
fun QueueScreen(
    departmentId: String,
    navigateToCreateTicket: () -> Unit,
    navigateToViewTicket: () -> Unit,
    navigateBack:()-> Unit,
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
            Scaffold(
                topBar = {
                    CurvedTopBar("Queue",true,navigateBack)
                }
            ){padding->
                Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                // --- Top Summary Cards ---
                Column(
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    BigSummaryCard(
                        title = "Currently Serving",
                        value = state.currentNumber,
                        color = Color(0xFF42A5F5),
                        icon = Icons.Default.Person
                    )
                    BigSummaryCard(
                        title = "Total Appointments Today",
                        value = state.totalPatients,
                        color = Color(0xFF66BB6A),
                        icon = Icons.Default.People
                    )
                    BigSummaryCard(
                        title = "Queue Ahead",
                        value = (state.totalPatients - state.currentNumber).coerceAtLeast(0),
                        color = Color(0xFFFFA726),
                        icon = Icons.Default.Schedule
                    )
                }

                // --- Queue Grid ---
                    QueueGridCard(
                        title = "Today's Queue",
                        ticketBoxes = state.ticketBoxes
                    )


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
                        Icon(Icons.Default.AddCircle, contentDescription = null)
                        Spacer(Modifier.width(8.dp))
                        Text("Book New Appointment", fontSize = 16.sp)
                    }

                    OutlinedButton(
                        onClick = navigateToViewTicket,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Icon(Icons.Default.ReceiptLong, contentDescription = null)
                        Spacer(Modifier.width(8.dp))
                        Text("View My Appointments", fontSize = 16.sp)
                    }
                }
            }
        }
        }
    }
}
@Composable
fun QueueGridCard(
    title: String,
    ticketBoxes: List<TicketBox>
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(12.dp))

            FlowRow(
                mainAxisSpacing = 6.dp,
                crossAxisSpacing = 6.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                ticketBoxes.forEach { box ->
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .size(42.dp)
                            .background(box.color, RoundedCornerShape(10.dp))
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
        }
    }
}

@Composable
fun BigSummaryCard(
    title: String,
    value: Int,
    color: Color,
    icon: ImageVector
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp),
        colors = CardDefaults.cardColors(containerColor = color),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            // Icon with background
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(
                        color.copy(alpha = 0.25f), // lighter shade
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(28.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Texts stacked vertically
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = title,
                    fontSize = 14.sp,
                    color = Color.White.copy(alpha = 0.9f)
                )
                Text(
                    text = value.toString(),
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }
    }
}





//@Composable
//fun QueueScreen(
//    departmentId: String,
//    navigateToCreateTicket: () -> Unit,
//    navigateToViewTicket: () -> Unit,
//    viewModel: QueueViewModel = hiltViewModel()
//) {
//    val uiState by viewModel.uiState.collectAsState()
//    LaunchedEffect(departmentId) {
//        viewModel.loadTodaysTickets(departmentId)
//    }
//
//    when (uiState) {
//        is QueueUiState.Loading -> {
//            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
//                CircularProgressIndicator()
//            }
//        }
//        is QueueUiState.Error -> {
//            val message = (uiState as QueueUiState.Error).message
//            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
//                Text(message, color = Color.Red, fontWeight = FontWeight.Bold)
//                Log.d("QueueScreen", "Error: $message")
//            }
//        }
//        is QueueUiState.Success -> {
//            val state = uiState as QueueUiState.Success
//            Column(
//                modifier = Modifier
//                    .fillMaxSize()
//                    .padding(16.dp),
//                verticalArrangement = Arrangement.spacedBy(24.dp)
//            ) {
//                // --- Top Summary Cards ---
//                Column(
//                    verticalArrangement = Arrangement.spacedBy(16.dp),
//                    modifier = Modifier.fillMaxWidth()
//                ) {
//                    BigSummaryCard("Now Treating", state.currentNumber, Color(0xFFFFC107))
//                    BigSummaryCard("Total Patients", state.totalPatients, Color(0xFF64B5F6))
//                    BigSummaryCard(
//                        "Patients Ahead of You",
//                        (state.totalPatients - state.currentNumber).coerceAtLeast(0),
//                        Color(0xFF81C784)
//                    )
//                }
//
//                // --- Queue Grid ---
//                FlowRow(
//                    mainAxisSpacing = 6.dp,
//                    crossAxisSpacing = 6.dp,
//                    modifier = Modifier.fillMaxWidth()
//                ) {
//                    state.ticketBoxes.forEach { box ->
//                        Box(
//                            contentAlignment = Alignment.Center,
//                            modifier = Modifier
//                                .size(40.dp)
//                                .background(box.color, RoundedCornerShape(8.dp))
//                        ) {
//                            Text(
//                                "${box.ticketNumber}",
//                                fontSize = 14.sp,
//                                fontWeight = FontWeight.Bold,
//                                color = Color.White
//                            )
//                        }
//                    }
//                }
//
//                // --- Quick Actions ---
//                Column(
//                    verticalArrangement = Arrangement.spacedBy(12.dp),
//                    modifier = Modifier.fillMaxWidth()
//                ) {
//                    Button(
//                        onClick = navigateToCreateTicket,
//                        modifier = Modifier.fillMaxWidth(),
//                        shape = RoundedCornerShape(16.dp)
//                    ) {
//                        Text("Book Appointment", fontSize = 16.sp)
//                    }
//
//                    OutlinedButton(
//                        onClick = navigateToViewTicket,
//                        modifier = Modifier.fillMaxWidth(),
//                        shape = RoundedCornerShape(16.dp)
//                    ) {
//                        Text("View My Tickets", fontSize = 16.sp)
//                    }
//                }
//            }
//        }
//    }
//}
//
//
////@Composable
////fun QueueScreen(
////    navigateToCreateTicket: () -> Unit,
////    navigateToViewTicket: () -> Unit,
////    viewModel: QueueViewModel=hiltViewModel()
////) {
////    val uistate by viewModel.uiState.collectAsState()
////    Column(
////        modifier = Modifier
////            .fillMaxSize()
////            .padding(16.dp),
////        verticalArrangement = Arrangement.spacedBy(24.dp)
////    ) {
////        // --- Top Summary Cards ---
////        Column(
////            verticalArrangement = Arrangement.spacedBy(16.dp),
////            modifier = Modifier.fillMaxWidth()
////        ) {
////            BigSummaryCard("Now Treating", currentNumber, Color(0xFFFFC107))
////            BigSummaryCard("Total Patients", totalPatients, Color(0xFF64B5F6))
////            BigSummaryCard(
////                "Patients Ahead of You",
////                (totalPatients - currentNumber).coerceAtLeast(0),
////                Color(0xFF81C784)
////            )
////        }
////
////
////        // --- Queue Grid ---
////        FlowRow(
////            mainAxisSpacing = 6.dp,
////            crossAxisSpacing = 6.dp,
////            modifier = Modifier.fillMaxWidth()
////        ) {
////            for (i in 1..totalPatients) {
////                val color = when {
////                    i < currentNumber -> Color(0xFF81C784)  // treated
////                    i == currentNumber -> Color(0xFFFFC107) // currently treating
////                    else -> Color(0xFF64B5F6)               // waiting
////                }
////
////                Box(
////                    contentAlignment = Alignment.Center,
////                    modifier = Modifier
////                        .size(40.dp)
////                        .background(color, RoundedCornerShape(8.dp))
////                ) {
////                    Text(
////                        "$i",
////                        fontSize = 14.sp,
////                        fontWeight = FontWeight.Bold,
////                        color = Color.White
////                    )
////                }
////            }
////        }
////        // --- Quick Actions ---
////        Column(
////            verticalArrangement = Arrangement.spacedBy(12.dp),
////            modifier = Modifier.fillMaxWidth()
////        ) {
////            Button(
////                onClick = navigateToCreateTicket,
////                modifier = Modifier.fillMaxWidth(),
////                shape = RoundedCornerShape(16.dp)
////            ) {
////                Text("Book Appointment", fontSize = 16.sp)
////            }
////
////            OutlinedButton(
////                onClick = navigateToViewTicket,
////                modifier = Modifier.fillMaxWidth(),
////                shape = RoundedCornerShape(16.dp)
////            ) {
////                Text("View My Tickets", fontSize = 16.sp)
////            }
////        }
////
////    }
////
////}
////
//@Composable
//fun BigSummaryCard(title: String, value: Int, color: Color) {
//    Card(
//        modifier = Modifier
//            .fillMaxWidth()
//            .height(100.dp),
//        colors = CardDefaults.cardColors(containerColor = color),
//        shape = RoundedCornerShape(16.dp),
//        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
//    ) {
//        Column(
//            horizontalAlignment = Alignment.CenterHorizontally,
//            verticalArrangement = Arrangement.Center,
//            modifier = Modifier.fillMaxSize()
//        ) {
//            Text(title, fontSize = 16.sp, color = Color.White)
//            Text(value.toString(), fontSize = 28.sp, fontWeight = FontWeight.Bold, color = Color.White)
//        }
//    }
//}

//@Preview
//@Composable
//fun PreviewQueueScreen() {
//    QueueScreen(
//
//        navigateToCreateTicket = {},
//        navigateToViewTicket = {}
//    )
//}
