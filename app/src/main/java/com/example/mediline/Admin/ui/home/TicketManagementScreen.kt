package com.example.mediline.Admin.ui.home


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.mediline.data.model.Form
import com.example.mediline.data.model.TicketStatus



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TicketManagementScreen(
    onCreateTicket: () -> Unit,
    onTicketClick: (Form) -> Unit,
    viewModel: AdminTicketViewModel = hiltViewModel()
) {
   val uiState by viewModel.uiState.collectAsState()
    val tickets = uiState.tickets

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Ticket Management") }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onCreateTicket,
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(Icons.Default.Add, contentDescription = "Create Ticket")
            }
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {

            // 🔎 FILTER BAR
            FilterSection(
                filters = uiState.filter,
                onFilterChange = { viewModel.setFilter(it) }
            )

            // 📋 TICKET LIST
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                items(uiState.filteredTickets) { ticket ->
                    TicketCard(
                        ticket = ticket,
                        onSkip = { viewModel.skipTicket(ticket.id,) },
                        onCancel = { viewModel.cancelTicket(ticket.id) },
                        onComplete = { viewModel.servingTicket(ticket.id) },
                        onReassign = { viewModel.reassignTicket(ticket.id) },
                        onClick = { onTicketClick(ticket) }
                    )
                }
            }
        }
    }
}


//@Composable
//fun FilterSection(filters: TicketFilters, onFilterChange: (TicketFilters) -> Unit) {
//    Column(modifier = Modifier.padding(8.dp)) {
//
//        // Search bar
//        OutlinedTextField(
//            value = filters.searchQuery,
//            onValueChange = { onFilterChange(filters.copy(searchQuery = it)) },
//            modifier = Modifier.fillMaxWidth(),
//            placeholder = { Text("Search by Ticket ID, Patient...") },
//            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) }
//        )
//
//        Spacer(Modifier.height(8.dp))
//
//        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
//            DropdownFilter(
//                label = "Department",
//                options = listOf("All", "Cardiology", "Dentistry", "Ortho"),
//                selected = filters.department,
//                onSelectedChange = { onFilterChange(filters.copy(department = it)) }
//            )
//            DropdownFilter(
//                label = "Status",
//                options = listOf("All", "Waiting", "Skipped", "Cancelled", "Completed"),
//                selected = filters.status,
//                onSelectedChange = { onFilterChange(filters.copy(status = it)) }
//            )
//        }
//    }
//}


@Composable
fun FilterSection(
    filters: TicketFilters,
    onFilterChange: (TicketFilters) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {

        // Department filter using Chips
        Text("DepartmentId", style = MaterialTheme.typography.labelMedium)
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            listOf("0","1","2","3").forEach { deptId ->
                val label = deptId
                FilterChip(
                    selected = filters.departmentId == deptId,
                    onClick = { onFilterChange(filters.copy(departmentId = deptId)) },
                    label = { Text(label) }
                )
            }
        }

        Text("Ticket Number", style = MaterialTheme.typography.labelMedium)
        OutlinedTextField(
            value = filters.todayCounter?.toString() ?: "",
            onValueChange = { input ->
                val value = input.toIntOrNull()
                onFilterChange(filters.copy(todayCounter = value))
            },
            label = { Text("Enter Ticket No") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
fun TicketCard(
    ticket: Form,
    onSkip: () -> Unit,
    onCancel: () -> Unit,
    onComplete: () -> Unit,
    onReassign: () -> Unit,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Ticket #${ticket.ticketNumber}", fontWeight = FontWeight.Bold)
                StatusChip(status = ticket.ticketStatus)
            }

            Spacer(Modifier.height(4.dp))
            Text("Patient: ${ticket.name}")
            Text("Id: ${ticket.id} ")
            Text("Queue No: ${ticket.ticketNumber}")

            Spacer(Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                if (ticket.ticketStatus== TicketStatus.ACTIVE) {
                    ActionButton("Skip", onSkip, Color.Yellow)
                    ActionButton("Cancel", onCancel, Color.Red)
                    ActionButton("Serving", onComplete, Color.Green)
                }
                if (ticket.ticketStatus == TicketStatus.CANCELLED || ticket.ticketStatus == TicketStatus.SKIPPED ) {
                    ActionButton("Re-assign", onReassign, Color.Blue)
                }

            }
        }
    }
}
@Composable
fun StatusChip(status: TicketStatus) {
    val color = when (status) {
        TicketStatus.ACTIVE -> Color(0xFF4CAF50)   // Green
        TicketStatus.CLOSED -> Color(0xFFF44336)   // Red
        TicketStatus.SKIPPED -> Color(0xFF9C27B0)  // Purple
        TicketStatus.NULL -> Color.LightGray       // Default
        TicketStatus.CANCELLED -> Color.DarkGray
        TicketStatus.EXPIRED -> Color.Blue
        TicketStatus.SERVING -> Color.Green
    }

    Box(
        modifier = Modifier
            .background(color, shape = RoundedCornerShape(8.dp))
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Text(
            text = status.name.replaceFirstChar { it.uppercase() },
            color = Color.White,
            fontSize = 12.sp
        )
    }
}


@Composable
fun ActionButton(text: String, onClick: () -> Unit, color: Color) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(containerColor = color),
        modifier = Modifier.height(32.dp),
        shape = RoundedCornerShape(8.dp),
        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 0.dp)
    ) {
        Text(text, fontSize = 12.sp, color = Color.White)
    }
}
@Preview(showBackground = true)
@Composable
fun ShowPrev(){
    TicketManagementScreen(onCreateTicket = {}, onTicketClick = {})
}


