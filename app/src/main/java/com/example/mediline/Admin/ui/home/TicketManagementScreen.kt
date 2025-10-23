package com.example.mediline.Admin.ui.home

import android.os.Build
import android.os.SecurityStateManager
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
import androidx.compose.material.icons.filled.Cake
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Search // Often used directly
import androidx.compose.material.icons.filled.Wc
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.mediline.Admin.ui.BeautifulLoader
import com.example.mediline.Admin.ui.CreateDepartment.CurvedTopBar
import com.example.mediline.Admin.ui.Screen
import com.example.mediline.data.model.Department
import com.example.mediline.data.model.PaymentStatus
import com.example.mediline.data.room.DepartmentEntity





@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TicketManagementScreen(
    onProfile:()->Unit,
    onTicketClick: (Form) -> Unit ,
    onCreateTicket: () -> Unit,
    onDepartments: () -> Unit,
    viewModel: AdminTicketViewModel = hiltViewModel()
)

{

    val uiState by viewModel.uiState.collectAsState()

    when{
        uiState.isLoading->
            {
                BeautifulLoader()
            }
        uiState.error != null -> {
            Text("Error: ${uiState.error}") // show error message
        }
        else -> {
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
                        }
                    }
                },
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
        TicketStatus.CLOSED -> Color(0xFF8BC34A)    // Light Green
        TicketStatus.CANCELLED -> Color(0xFFE57373) // Light Red
        TicketStatus.SKIPPED -> Color(0xFFFFC947)   // Light Orange

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
    onBack: () -> Unit
) {
    Scaffold(
        topBar = { CurvedTopBar("Ticket Details", true, onBack) }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // ✅ Ticket Summary Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(20.dp),
                elevation = CardDefaults.cardElevation(8.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text(
                        "Ticket No.: ${ticket.ticketNumber}",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        StatusChip("Ticket: ${ticket.ticketStatus}", ticket.ticketStatus.toColor())
                        StatusChip("Payment: ${ticket.paymentStatus}", ticket.paymentStatus.toColor())
                    }
                }
            }

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(20.dp),
                elevation = CardDefaults.cardElevation(8.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    SectionHeader("Personal Info")
                    DetailRow(icon = Icons.Default.Person, label = "Name", value = ticket.name)
                    DetailRow(icon = Icons.Default.Cake, label = "Age", value = ticket.age.toString())
                    DetailRow(icon = Icons.Default.Wc, label = "Sex", value = ticket.sex.toString())
                    DetailRow(icon = Icons.Default.Person, label = "Father's Name", value = ticket.fatherName)

                    SectionHeader("Contact Info")
                    DetailRow(icon = Icons.Default.Phone, label = "Mobile No.", value = ticket.phone)
                    DetailRow(icon = Icons.Default.Home, label = "Address", value = ticket.address)

                    SectionHeader("Ticket Info")
                    DetailRow(icon = Icons.Default.Schedule, label = "Time Stamp", value = ticket.timeStamp.toString())
                    DetailRow(
                        icon = Icons.Default.Info,
                        label = "Department",
                        value = departments.find { it.id == ticket.departmentId }?.name ?: ""
                    )
                }
            }
        }
    }
}

//
@Composable
fun SectionHeader(title: String) {
    Text(
        title,
        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
        color = Color(0xFF3BB77E)
    )
}

// ✅ Detail Row with icon and proper spacing
@Composable
fun DetailRow(icon: ImageVector, label: String, value: String) {
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        Icon(icon, contentDescription = null, tint = Color(0xFF3BB77E))
        Column {
            Text(label, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
            Text(value, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Medium)
        }
    }
}

// ✅ Status Chip for ticket/payment status
@Composable
fun StatusChip(text: String, color: Color) {
    Box(
        modifier = Modifier
            .background(color.copy(alpha = 0.2f), RoundedCornerShape(16.dp))
            .padding(horizontal = 14.dp, vertical = 8.dp)
    ) {
        Text(text, color = color, style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Bold)
    }
}

// ✅ Action Button with rounded corners and color
@Composable
fun TicketActionButton(text: String, action: TicketAction, onClick: (TicketAction) -> Unit) {
    Button(
        onClick = { onClick(action) },
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3BB77E)),
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.height(48.dp)
    ) {
        Text(text, color = Color.White, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold)
    }
}

// ✅ Color mapping
fun TicketStatus.toColor(): Color = when (this) {
    TicketStatus.CLOSED -> Color(0xFF8BC34A)
    TicketStatus.CANCELLED -> Color(0xFFE57373)
    TicketStatus.SKIPPED -> Color(0xFFFFC947)
    else -> Color.Gray
}

fun PaymentStatus.toColor(): Color = when (this) {
    PaymentStatus.PAID -> Color(0xFF8BC34A)
    PaymentStatus.UNPAID -> Color(0xFFFFC947)
    else -> Color.Gray
}

// ✅ Actions enum
enum class TicketAction { SERVE, COMPLETE, SKIP, REASSIGN }
