package com.example.mediline.Admin.ui.home


import android.os.Build
import androidx.annotation.RequiresApi
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions

import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.mediline.data.model.Form
import com.example.mediline.data.model.TicketStatus
 import androidx.compose.material3.OutlinedTextField
 import androidx.compose.material3.Text
 import androidx.compose.material3.Icon
 import androidx.compose.material.icons.Icons // For Icons.Default.Search
 import androidx.compose.material.icons.filled.Search // Often used directly
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
 import androidx.compose.ui.text.TextStyle
 import androidx.compose.ui.unit.sp

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TicketManagementScreen(
    onTicketClick: (Form) -> Unit,
    viewModel: AdminTicketViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Tickets",
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                        color = Color.White
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF3BB77E) // ✅ PrimaryGreen
                )
            )
        },
       bottomBar = {
           BottomNavBar({})
       },
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            // ✅ Compact Search Bar inside card
            SearchSection()

            // ✅ Ticket list
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(uiState.filteredTickets) { ticket ->
                    TicketCard(
                        ticket = ticket,
                        onSkip = { viewModel.skipTicket(ticket.id) },
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
/* 🔹 Bottom Navigation */
@Composable
fun BottomNavBar(onNavigate: (String) -> Unit) {
    NavigationBar(containerColor = Color.White) {
        NavigationBarItem(
            icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
            label = { Text("Home") },
            selected = false,
            onClick = { onNavigate("home") }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Person, contentDescription = "Profile") },
            label = { Text("Profile") },
            selected = false,
            onClick = { onNavigate("profile") }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.AddCircle, contentDescription = "Create Ticket") },
            label = { Text("New Ticket") },
            selected = false,
            onClick = { onNavigate("create_ticket") }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.List, contentDescription = "Departments") },
            label = { Text("Departments") },
            selected = false,
            onClick = { onNavigate("departments") }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchSection() {
    var department by remember { mutableStateOf("Department") }
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 6.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.background
        ),
        elevation = CardDefaults.cardElevation(1.dp)
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Department Dropdown (full width, first row)
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded },
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = department,
                    onValueChange = {},
                    readOnly = true,
                    placeholder = { Text("Department") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                        focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                        unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant
                    ),
                    shape = RoundedCornerShape(8.dp)
                )

                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    listOf("Cardiology", "Neurology", "Orthopedics", "Dermatology").forEach {
                        DropdownMenuItem(
                            text = { Text(it) },
                            onClick = {
                                department = it
                                expanded = false
                            }
                        )
                    }
                }
            }

            // Row for Query and Ticket No
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = "",
                    onValueChange = {},
                    placeholder = { Text("Patient") },
                    singleLine = true,
                    modifier = Modifier.weight(1f),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                        focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                        unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant
                    ),
                    shape = RoundedCornerShape(8.dp)
                )

                OutlinedTextField(
                    value = "",
                    onValueChange = {},
                    placeholder = { Text("Ticket No") },
                    singleLine = true,
                    modifier = Modifier.weight(0.8f),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                        focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                        unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant
                    ),
                    shape = RoundedCornerShape(8.dp)
                )
            }
        }
    }
}




/* ✅ Ticket Card */
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
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(6.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "👤 ${ticket.name}", // ✅ Patient name shown
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleMedium
                )
                StatusChip(ticket.ticketStatus) // ✅ status chip
            }

            Spacer(Modifier.height(6.dp))
            Text("Dept: ${ticket.departmentId}", style = MaterialTheme.typography.bodySmall)
            Text("Queue No: ${ticket.ticketNumber}", style = MaterialTheme.typography.bodySmall)

            Spacer(Modifier.height(12.dp))

            // ✅ Action Buttons
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                if (ticket.ticketStatus == TicketStatus.ACTIVE) {
                    ActionButton("Skip", onSkip, Color(0xFFFFB300))
                    ActionButton("Cancel", onCancel, Color(0xFFE53935))
                    ActionButton("Serving", onComplete, Color(0xFF43A047))
                }
                if (ticket.ticketStatus == TicketStatus.CANCELLED || ticket.ticketStatus == TicketStatus.SKIPPED) {
                    ActionButton("Re-assign", onReassign, Color(0xFF1E88E5))
                }
            }
        }
    }
}

/* ✅ Action Button */
@Composable
fun ActionButton(text: String, onClick: () -> Unit, color: Color) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(containerColor = color),
        modifier = Modifier.height(36.dp),
        shape = RoundedCornerShape(8.dp),
        contentPadding = PaddingValues(horizontal = 12.dp)
    ) {
        Text(text, fontSize = 12.sp, color = Color.White)
    }
}
@Composable
fun StatusChip(status: TicketStatus) {
    val (color, text) = when (status) {
        TicketStatus.ACTIVE -> Color(0xFF4CAF50) to "Active"
        TicketStatus.CLOSED -> Color(0xFFF44336) to "Closed"
        TicketStatus.SKIPPED -> Color(0xFF9C27B0) to "Skipped"
        TicketStatus.CANCELLED -> Color(0xFF757575) to "Cancelled"
        TicketStatus.EXPIRED -> Color(0xFF2196F3) to "Expired"
        TicketStatus.SERVING -> Color(0xFFFF9800) to "Serving"
        TicketStatus.NULL -> Color.LightGray to "Unknown"
    }

    Box(
        modifier = Modifier
            .background(color, shape = RoundedCornerShape(12.dp))
            .padding(horizontal = 10.dp, vertical = 4.dp)
    ) {
        Text(
            text = text,
            color = Color.White,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}


//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun TicketManagementScreen(
//    onCreateTicket: () -> Unit,
//    onTicketClick: (Form) -> Unit,
//    viewModel: AdminTicketViewModel = hiltViewModel()
//) {
//    val uiState by viewModel.uiState.collectAsState()
//
//    Scaffold(
//        topBar = {
//            TopAppBar(
//                title = {
//                    Text(
//                        "Tickets",
//                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
//                    )
//                },
//                colors = TopAppBarDefaults.topAppBarColors(
//                    containerColor = Color(0xFF1E88E5), // Blue for hospital admin
//                    titleContentColor = Color.White
//                )
//            )
//        },
//        floatingActionButton = {
//            FloatingActionButton(
//                onClick = onCreateTicket,
//                containerColor = Color(0xFF43A047) // Green accent
//            ) {
//                Icon(Icons.Default.Add, contentDescription = "Create Ticket", tint = Color.White)
//            }
//        },
//        containerColor = Color(0xFFF5F7FA) // Soft hospital background
//    ) { padding ->
//        Column(modifier = Modifier.padding(padding).fillMaxSize()) {
//
//            // Filter section in a card
//            Card(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(12.dp),
//                shape = RoundedCornerShape(12.dp),
//                colors = CardDefaults.cardColors(containerColor = Color.White),
//                elevation = CardDefaults.cardElevation(4.dp)
//            ) {
//                Column(Modifier.padding(12.dp)) {
//                    FilterSection(
//                        filters = uiState.filter,
//                        onFilterChange = { viewModel.setFilter(it) }
//                    )
//                }
//            }
//
//            // Ticket list
//            LazyColumn(
//                modifier = Modifier.fillMaxSize(),
//                contentPadding = PaddingValues(12.dp),
//                verticalArrangement = Arrangement.spacedBy(12.dp)
//            ) {
//                items(uiState.filteredTickets) { ticket ->
//                    TicketCard(
//                        ticket = ticket,
//                        onSkip = { viewModel.skipTicket(ticket.id) },
//                        onCancel = { viewModel.cancelTicket(ticket.id) },
//                        onComplete = { viewModel.servingTicket(ticket.id) },
//                        onReassign = { viewModel.reassignTicket(ticket.id) },
//                        onClick = { onTicketClick(ticket) }
//                    )
//                }
//            }
//        }
//    }
//}
//
///* ✅ Filter Section included inline */
//@Composable
//fun FilterSection(
//    filters: TicketFilters,
//    onFilterChange: (TicketFilters) -> Unit
//) {
//    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
//
//        Text("Departments", style = MaterialTheme.typography.labelMedium)
//
//        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
//            items(listOf("Cardiology", "Dentistry", "Ortho", "General", "Neurology", "ENT", "Dermatology")) { dept ->
//                FilterChip(
//                    selected = filters.department == dept,
//                    onClick = { onFilterChange(filters.copy(department = dept)) },
//                    label = { Text(dept) }
//                )
//            }
//        }
//
//        Text("Ticket Number", style = MaterialTheme.typography.labelMedium)
//        OutlinedTextField(
//            value = filters.todayCounter?.toString() ?: "",
//            onValueChange = { input ->
//                val value = input.toIntOrNull()
//                onFilterChange(filters.copy(todayCounter = value))
//            },
//            label = { Text("Enter Ticket No") },
//            singleLine = true,
//            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
//            modifier = Modifier.fillMaxWidth()
//        )
//    }
//}
//
///* ✅ Ticket Card */
//@Composable
//fun TicketCard(
//    ticket: Form,
//    onSkip: () -> Unit,
//    onCancel: () -> Unit,
//    onComplete: () -> Unit,
//    onReassign: () -> Unit,
//    onClick: () -> Unit
//) {
//    Card(
//        modifier = Modifier
//            .fillMaxWidth()
//            .clickable { onClick() },
//        shape = RoundedCornerShape(16.dp),
//        elevation = CardDefaults.cardElevation(6.dp),
//        colors = CardDefaults.cardColors(containerColor = Color.White)
//    ) {
//        Column(modifier = Modifier.padding(16.dp)) {
//            Row(
//                horizontalArrangement = Arrangement.SpaceBetween,
//                modifier = Modifier.fillMaxWidth()
//            ) {
//                Text(
//                    "Ticket #${ticket.ticketNumber}",
//                    fontWeight = FontWeight.Bold,
//                    style = MaterialTheme.typography.titleMedium
//                )
//                StatusChip(status = ticket.ticketStatus)
//            }
//
//            Spacer(Modifier.height(8.dp))
//            Text("👤 Patient: ${ticket.name}", style = MaterialTheme.typography.bodyMedium)
//            Text("🆔 ID: ${ticket.id}", style = MaterialTheme.typography.bodySmall)
//            Text("🔢 Queue No: ${ticket.ticketNumber}", style = MaterialTheme.typography.bodySmall)
//
//            Spacer(Modifier.height(12.dp))
//            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
//                if (ticket.ticketStatus == TicketStatus.ACTIVE) {
//                    ActionButton("Skip", onSkip, Color(0xFFFFB300))
//                    ActionButton("Cancel", onCancel, Color(0xFFE53935))
//                    ActionButton("Serving", onComplete, Color(0xFF43A047))
//                }
//                if (ticket.ticketStatus == TicketStatus.CANCELLED || ticket.ticketStatus == TicketStatus.SKIPPED) {
//                    ActionButton("Re-assign", onReassign, Color(0xFF1E88E5))
//                }
//            }
//        }
//    }
//}
//
///* ✅ Status Chip */
//@Composable
//fun StatusChip(status: TicketStatus) {
//    val color = when (status) {
//        TicketStatus.ACTIVE -> Color(0xFF4CAF50)
//        TicketStatus.CLOSED -> Color(0xFFF44336)
//        TicketStatus.SKIPPED -> Color(0xFF9C27B0)
//        TicketStatus.NULL -> Color.LightGray
//        TicketStatus.CANCELLED -> Color.DarkGray
//        TicketStatus.EXPIRED -> Color.Blue
//        TicketStatus.SERVING -> Color.Green
//    }
//
//    Box(
//        modifier = Modifier
//            .background(color, shape = RoundedCornerShape(8.dp))
//            .padding(horizontal = 8.dp, vertical = 4.dp)
//    ) {
//        Text(
//            text = status.name.replaceFirstChar { it.uppercase() },
//            color = Color.White,
//            fontSize = 12.sp
//        )
//    }
//}
//
///* ✅ Action Button inline */
//@Composable
//fun ActionButton(text: String, onClick: () -> Unit, color: Color) {
//    Button(
//        onClick = onClick,
//        colors = ButtonDefaults.buttonColors(containerColor = color),
//        modifier = Modifier.height(36.dp),
//        shape = RoundedCornerShape(8.dp),
//        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 0.dp)
//    ) {
//        Text(text, fontSize = 12.sp, color = Color.White)
//    }
//}
//
//@Preview(showBackground = true)
//@Composable
//fun ShowPrev() {
//    TicketManagementScreen(onCreateTicket = {}, onTicketClick = {})
//}
//
