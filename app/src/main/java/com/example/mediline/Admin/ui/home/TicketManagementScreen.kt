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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll

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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search // Often used directly
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.mediline.Admin.ui.Screen
import com.example.mediline.data.model.Department
import com.example.mediline.data.room.DepartmentEntity



@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TicketManagementScreen(
    onBack: () -> Unit,
    onProfile:()->Unit,
    onTicketClick: (Form) -> Unit ,
    onCreateTicket: () -> Unit,
    onDepartments: () -> Unit,
    viewModel: AdminTicketViewModel = hiltViewModel()
)

{

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
                    containerColor = Color(0xFF3BB77E)
                )
            )
        },
        bottomBar = {
            BottomNavBar { route ->
                when (route) {
                    Screen.Home.route -> {} // already here
                    Screen.CreateTicket.route -> onCreateTicket()
                    Screen.Departments.route -> onDepartments()
                    Screen.Profile.route -> onProfile()
                } }
        }
        ,
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            // ✅ Compact Search Bar inside card
            SearchSection(
                departments = uiState.departments,
                filter = uiState.filter,
                onFilterChanged = { newFilter ->
                    viewModel.setFilter(newFilter)
                }
            )


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
                        onComplete = { viewModel.completeTicket(ticket.id) },
                        onReassign = { viewModel.reassignTicket(ticket.id) },
                        onClick = { onTicketClick(ticket) },
                        departments = uiState.departments
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
            label = { Text("Home") }, selected = false,
            onClick = { onNavigate("home") }
        )

        NavigationBarItem(
            icon = { Icon(Icons.Default.AddCircle, contentDescription = "Create Ticket") },
            label = { Text("New Ticket") },
            selected = false, onClick = { onNavigate("create_ticket") }
        )
        NavigationBarItem( icon = { Icon(Icons.Default.List, contentDescription = "Departments") },
            label = { Text("Departments") }, selected = false,
            onClick = { onNavigate("departments") }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Person, contentDescription = "Profile") },
            label = { Text("Profile") },
            selected = false, onClick = { onNavigate("profile") }
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchSection(
    departments: List<Department>,
    modifier: Modifier = Modifier,
    filter: TicketFilters, // current filter from uiState
    onFilterChanged: (TicketFilters) -> Unit // callback to update ViewModel
) {
    var expanded by remember { mutableStateOf(false) }
    var selectedDepartment by remember { mutableStateOf(filter.department) }

    // Add an "All" department with id = "" at the start
    val departmentsWithAll = listOf(Department(id = "", name = "All Departments")) + departments

    Card(
        modifier = modifier
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
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Department Dropdown
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded },
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = if (selectedDepartment.isBlank()) "" else departmentsWithAll.find { it.id == selectedDepartment }?.name ?: "",
                    onValueChange = {},
                    readOnly = true,
                    placeholder = { Text("Departments") }, // Show placeholder if none selected
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
                    departmentsWithAll.forEach { department ->
                        DropdownMenuItem(
                            text = { Text(department.name) },
                            onClick = {
                                selectedDepartment = department.id
                                expanded = false
                                // If id is "", it means "All", show all tickets
                                onFilterChanged(filter.copy(department = department.id))
                            }
                        )
                    }
                }
            }

            // Search inputs (Name + Ticket No)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedTextField(
                    value = filter.searchQuery,
                    onValueChange = { query ->
                        onFilterChanged(filter.copy(searchQuery = query))
                    },
                    label = { Text("Name") },
                    modifier = Modifier.weight(1f),
                    singleLine = true,
                    shape = RoundedCornerShape(8.dp)
                )

                OutlinedTextField(
                    value = filter.todayCounter?.toString() ?: "",
                    onValueChange = { text ->
                        val number = text.toIntOrNull()
                        onFilterChanged(filter.copy(todayCounter = number))
                    },
                    label = { Text("Ticket No") },
                    modifier = Modifier.weight(1f),
                    singleLine = true,
                    shape = RoundedCornerShape(8.dp),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
            }
        }
    }
}



//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun SearchSection(
//    departments: List<Department>,
//    modifier: Modifier = Modifier,
//    filter: TicketFilters, // ✅ current filter from uiState
//    onFilterChanged: (TicketFilters) -> Unit // ✅ callback to update ViewModel
//) {
//    var expanded by remember { mutableStateOf(false) }
//    var selectedDepartment by remember { mutableStateOf(filter.department) }
//
//    Card(
//        modifier = modifier
//            .fillMaxWidth()
//            .padding(horizontal = 12.dp, vertical = 6.dp),
//        shape = RoundedCornerShape(12.dp),
//        colors = CardDefaults.cardColors(
//            containerColor = MaterialTheme.colorScheme.background
//        ),
//        elevation = CardDefaults.cardElevation(1.dp)
//    ) {
//        Column(
//            modifier = Modifier.padding(8.dp),
//            verticalArrangement = Arrangement.spacedBy(12.dp)
//        ) {
//            // ✅ Department Dropdown
//            ExposedDropdownMenuBox(
//                expanded = expanded,
//                onExpandedChange = { expanded = !expanded },
//                modifier = Modifier.fillMaxWidth()
//            ) {
//                OutlinedTextField(
//                    value = departments.find { it.id == selectedDepartment }?.name ?: "",
//                    onValueChange = {},
//                    readOnly = true,
//                    placeholder = { Text("Department") },
//                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
//                    modifier = Modifier
//                        .menuAnchor()
//                        .fillMaxWidth(),
//                    colors = OutlinedTextFieldDefaults.colors(
//                        focusedBorderColor = MaterialTheme.colorScheme.primary,
//                        unfocusedBorderColor = MaterialTheme.colorScheme.outline,
//                        focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
//                        unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant
//                    ),
//                    shape = RoundedCornerShape(8.dp)
//                )
//
//                ExposedDropdownMenu(
//                    expanded = expanded,
//                    onDismissRequest = { expanded = false }
//                ) {
//                    departments.forEach { department ->
//                        DropdownMenuItem(
//                            text = { Text(department.name) },
//                            onClick = {
//                                selectedDepartment = department.id
//                                expanded = false
//                                onFilterChanged(filter.copy(department = department.id.toString()))
//
//                            }
//                        )
//                    }
//                }
//            }
//
//            // ✅ Search inputs (Name + Ticket No)
//            Row(
//                modifier = Modifier.fillMaxWidth(),
//                horizontalArrangement = Arrangement.spacedBy(12.dp)
//            ) {
//                OutlinedTextField(
//                    value = filter.searchQuery,
//                    onValueChange = { query ->
//                        onFilterChanged(filter.copy(searchQuery = query))
//                    },
//                    label = { Text("Name") },
//                    modifier = Modifier.weight(1f),
//                    singleLine = true,
//                    shape = RoundedCornerShape(8.dp)
//                )
//
//                OutlinedTextField(
//                    value = filter.todayCounter?.toString() ?: "",
//                    onValueChange = { text ->
//                        val number = text.toIntOrNull()
//                        onFilterChanged(filter.copy(todayCounter = number))
//                    },
//                    label = { Text("Ticket No") },
//                    modifier = Modifier.weight(1f),
//                    singleLine = true,
//                    shape = RoundedCornerShape(8.dp),
//                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
//                )
//            }
//        }
//    }
//}




/* ✅ Ticket Card */@Composable
fun TicketCard(
    departments: List<Department>,
    ticket: Form,
    onSkip: () -> Unit,
    onCancel: () -> Unit,
    onReassign: () -> Unit,
    onComplete: () -> Unit,
    onClick: () -> Unit
) {
    val cardColor = when (ticket.ticketStatus) {
        TicketStatus.CLOSED -> Color(0xFF60A462) // Green
        TicketStatus.CANCELLED -> Color(0xFFF44336) // Red
        TicketStatus.SKIPPED -> Color(0xFFFFB300) // Orange
        else -> Color.White
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(6.dp),
        colors = CardDefaults.cardColors(containerColor = cardColor)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "👤 ${ticket.name}",
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleMedium
                )
                StatusChip(ticket.ticketStatus)
            }

            Spacer(Modifier.height(6.dp))
            Text(
                "Department: ${departments.find { it.id == ticket.departmentId }?.name}",
                style = MaterialTheme.typography.bodySmall
            )
            Text("Ticket No: ${ticket.ticketNumber}", style = MaterialTheme.typography.bodySmall)

            Spacer(Modifier.height(12.dp))

            // ✅ Actions controlled by status
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                when (ticket.ticketStatus) {
                    TicketStatus.ACTIVE -> {
                        ActionButton("Skip", onSkip, Color(0xFFFFB300))
                        ActionButton("Cancel", onCancel, Color(0xFFF44336))
                        ActionButton("Complete", onComplete, Color(0xFF60A462))
                    }

                    TicketStatus.CLOSED,
                    TicketStatus.CANCELLED,
                    TicketStatus.SKIPPED -> {
                        ActionButton("Re-assign", onReassign, Color(0xFF1E88E5))
                    }

                    else -> {}
                }
            }
        }
    }
}

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
        TicketStatus.SERVING-> Color(0xFF1E88E5) to "Pending"
        TicketStatus.CLOSED -> Color(0xFF60A462) to "Closed"
        TicketStatus.SKIPPED -> Color(0xFFFFB300) to "Skipped"
        TicketStatus.CANCELLED -> Color(0xFFF44336) to "Cancelled"
        TicketStatus.EXPIRED -> Color(0xFF140E1A) to "Expired"
        TicketStatus.ACTIVE -> Color(0xFF2196F3) to "Active"
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
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TicketDetailScreen(
    departments: List<Department>,
    ticket: Form,
    onBack: () -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Ticket Details",
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                        color = Color.White
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF3BB77E)
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // ✅ Ticket Summary
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF6F6F6)),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Ticket No. : ${ticket.ticketNumber}", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold)
                    Spacer(Modifier.height(4.dp))
                }
            }

            Spacer(Modifier.height(16.dp))

            // ✅ Ticket Details
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    DetailRow(label = "Department", value = departments.find { it.id == ticket.departmentId }?.name ?: "")
                    DetailRow(label = "Name", value = ticket.name)
                    DetailRow(label = "Age", value = ticket.age.toString())
                    DetailRow(label = "Sex", value = ticket.sex.toString())
                    DetailRow(label = "Father's Name", value = ticket.fatherName)
                    DetailRow(label = "Mobile No.", value = ticket.phone)
                    DetailRow(label = "Address", value = ticket.address)
                    DetailRow(label = "Time Stamp", value = ticket.timeStamp.toString())
                    DetailRow(label = "Ticket Status", value = ticket.ticketStatus.toString())
                    DetailRow(label = "Payment Status", value = ticket.paymentStatus.toString())

                }
            }
        }
    }
}

@Composable
fun DetailRow(label: String, value: String) {
    Column {
        Text(label, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
        Text(value, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Medium)
    }
}

