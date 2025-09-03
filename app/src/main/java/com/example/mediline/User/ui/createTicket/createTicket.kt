package com.example.mediline.User.ui.createTicket

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*

import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cake
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Wc
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.mediline.User.ui.authentication.CurvedTopBar




@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)
@Composable
fun RegistrationScreen(
    deptId: String,
    navigateToPayment: () -> Unit,
    navigateBack:()-> Unit,
    viewModel: CreateTicketViewModel = hiltViewModel()
) {
    LaunchedEffect(deptId) {
        viewModel.loadDepartment(deptId)
        viewModel.updateDeptId(deptId)
    }

    val department by viewModel.department.collectAsState()
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(viewModel) {
        viewModel.eventFlow.collect { event ->
            when (event) {
                is UiEvent.NavigateToPayment -> navigateToPayment()
                is UiEvent.ShowError -> Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    val bloodGroups = listOf("A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-")
    var genderExpanded by remember { mutableStateOf(false) }
    var bloodGroupExpanded by remember { mutableStateOf(false) }

    Scaffold(
        topBar = { CurvedTopBar("Patient Registration",true,  navigateBack) },
        bottomBar = {
            Text(
                text = "© 2025 Your Hospital Registration",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                textAlign = TextAlign.Center
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // Title

            // Doctor / Dept / Fee Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {

                    Text(
                        text = "Department: ${department?.name ?: "-"}",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Medium
                    )
                    //Spacer(Modifier.height(8.dp)) // sma
                    Text(
                        text = "Doctor: ${department?.doctor ?: "-"}",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Medium
                    )

                    Spacer(Modifier.height(4.dp)) // same gap
                    Text(
                        text = "Registration Fee: ₹${department?.fees ?: "-"}",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.primary
                    )
                }

            }

            // Form Fields
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                OutlinedTextField(
                    value = uiState.name,
                    onValueChange = { viewModel.updateName(it) },
                    label = { Text("Patient Name") },
                    singleLine = true,
                    leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )

                OutlinedTextField(
                    value = uiState.address,
                    onValueChange = { viewModel.updateAddress(it) },
                    label = { Text("Address") },
                    singleLine = true,
                    leadingIcon = { Icon(Icons.Default.Home, contentDescription = null) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )

                OutlinedTextField(
                    value = uiState.phone,
                    onValueChange = { viewModel.updatePhone(it) },
                    label = { Text("Contact Number") },
                    singleLine = true,
                    leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )

                OutlinedTextField(
                    value = uiState.age.toString(),
                    onValueChange = { viewModel.updateAge(it) },
                    label = { Text("Age") },
                    singleLine = true,
                    leadingIcon = { Icon(Icons.Default.Cake, contentDescription = null) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )

                // Gender Dropdown
                ExposedDropdownMenuBox(
                    expanded = genderExpanded,
                    onExpandedChange = { genderExpanded = !genderExpanded }
                ) {
                    OutlinedTextField(
                        value = uiState.sex.name,
                        onValueChange = {},
                        label = { Text("Gender") },
                        readOnly = true,
                        leadingIcon = { Icon(Icons.Default.Wc, contentDescription = null) },
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = genderExpanded)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor(),
                        shape = RoundedCornerShape(12.dp)
                    )

                    ExposedDropdownMenu(
                        expanded = genderExpanded,
                        onDismissRequest = { genderExpanded = false }
                    ) {
                        Sex.entries.forEach { sex ->
                            DropdownMenuItem(
                                text = { Text(sex.name) },
                                onClick = {
                                    viewModel.updateSex(sex)
                                    genderExpanded = false
                                }
                            )
                        }
                    }
                }

                // Optional Blood Group
                ExposedDropdownMenuBox(
                    expanded = bloodGroupExpanded,
                    onExpandedChange = { bloodGroupExpanded = !bloodGroupExpanded }
                ) {
                    OutlinedTextField(
                        value = uiState.bloodGroup ?: "",
                        onValueChange = {},
                        label = { Text("Blood Group (Optional)") },
                        readOnly = true,
                        leadingIcon = { Icon(Icons.Default.Favorite, contentDescription = null) },
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = bloodGroupExpanded)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor(),
                        shape = RoundedCornerShape(12.dp)
                    )

                    ExposedDropdownMenu(
                        expanded = bloodGroupExpanded,
                        onDismissRequest = { bloodGroupExpanded = false }
                    ) {
                        bloodGroups.forEach { bg ->
                            DropdownMenuItem(
                                text = { Text(bg) },
                                onClick = {
                                    viewModel.updateBloodGroup(bg)
                                    bloodGroupExpanded = false
                                }
                            )
                        }
                    }
                }
            }

            // Submit Button
            Button(
                onClick = { viewModel.addFormFromState() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(Icons.Default.CheckCircle, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text(text = "Proceed to Pay ₹${department?.fees ?: "-"}")
            }
        }
    }
}

//
//@Composable
//fun RegistrationScreen(
//    deptId: String,
//    navigateToPayment: () -> Unit,
//    viewModel: CreateTicketViewModel = hiltViewModel()
//) {
//    LaunchedEffect(deptId) {
//        viewModel.loadDepartment(deptId)
//    }
//    val department = viewModel.department.collectAsState().value
//    val context = LocalContext.current
//    viewModel.updateDeptId(deptId)
//
//    LaunchedEffect(viewModel) {
//        viewModel.eventFlow.collect { event ->
//            when (event) {
//                is UiEvent.NavigateToPayment -> navigateToPayment()
//                is UiEvent.ShowError -> Toast.makeText(context, event.message, Toast.LENGTH_SHORT)
//                    .show()
//            }
//        }
//    }
//
//    val uiState by viewModel.uiState.collectAsState()
//
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .padding(16.dp)
//            .verticalScroll(rememberScrollState())
//    ) {
//        Text(
//            text = "Patient Registration",
//            style = MaterialTheme.typography.headlineMedium,
//            fontWeight = FontWeight.Bold,
//            color = Color(0xFF0277BD) // hospital-blue
//        )
//
//        Spacer(modifier = Modifier.height(12.dp))
//
//        Card(
//            shape = RoundedCornerShape(16.dp),
//            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
//            modifier = Modifier.fillMaxWidth()
//        ) {
//            Column(modifier = Modifier.padding(16.dp)) {
//                Text(
//                    text = "Doctor: ${department?.doctor ?: "-"}",
//                    style = MaterialTheme.typography.titleMedium
//                )
//                Text(
//                    text = "Department: ${department?.name ?: "-"}",
//                    style = MaterialTheme.typography.bodyLarge
//                )
//                Spacer(modifier = Modifier.height(8.dp))
//                Text(
//                    text = "Registration Fee: ₹${department?.fees ?: "-"}",
//                    style = MaterialTheme.typography.bodyLarge,
//                    fontWeight = FontWeight.Bold
//                )
//
//                Spacer(modifier = Modifier.height(16.dp))
//
//                OutlinedTextField(
//                    value = uiState.name,
//                    onValueChange = { viewModel.updateName(it) },
//                    label = { Text("Patient Name") },
//                    modifier = Modifier.fillMaxWidth()
//                )
//
//                Spacer(modifier = Modifier.height(12.dp))
//
//                OutlinedTextField(
//                    value = uiState.address,
//                    onValueChange = { viewModel.updateAddress(it) },
//                    label = { Text("Address") },
//                    modifier = Modifier.fillMaxWidth()
//                )
//
//                Spacer(modifier = Modifier.height(12.dp))
//
//                OutlinedTextField(
//                    value = uiState.phone,
//                    onValueChange = { viewModel.updatePhone(it) },
//                    label = { Text("Contact Number") },
//                    modifier = Modifier.fillMaxWidth(),
//                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
//                )
//
//                Spacer(modifier = Modifier.height(12.dp))
//
//                OutlinedTextField(
//                    value = uiState.age.toString(),
//                    onValueChange = { viewModel.updateAge(it) },
//                    label = { Text("Age") },
//                    modifier = Modifier.fillMaxWidth(),
//                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
//                )
//
//                Spacer(modifier = Modifier.height(12.dp))
//
//                // Original Gender Dropdown
//                var genderExpanded by remember { mutableStateOf(false) }
//                Box {
//                    OutlinedTextField(
//                        value = uiState.sex.name,
//                        onValueChange = {},
//                        readOnly = true,
//                        label = { Text("Gender") },
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .clickable { genderExpanded = true },
//                        trailingIcon = {
//                            Icon(
//                                imageVector = if (genderExpanded) Icons.Default.KeyboardArrowUp
//                                else Icons.Default.KeyboardArrowDown,
//                                contentDescription = null,
//                                tint = MaterialTheme.colorScheme.primary
//                            )
//                        },
//
//
//                                shape = RoundedCornerShape(12.dp)
//                    )
//
//                    DropdownMenu(
//                        expanded = genderExpanded,
//                        onDismissRequest = { genderExpanded = false },
//                        modifier = Modifier.fillMaxWidth()
//                    ) {
//                        Sex.entries.forEach { sex ->
//                            DropdownMenuItem(
//                                text = { Text(sex.name) },
//                                onClick = {
//                                    viewModel.updateSex(sex)
//                                    genderExpanded = false
//                                }
//                            )
//                        }
//                    }
//                }
//            }
//        }
//
//        Spacer(modifier = Modifier.height(20.dp))
//
//        Button(
//            onClick = { viewModel.addFormFromState() },
//            modifier = Modifier
//                .fillMaxWidth()
//                .height(50.dp),
//            shape = RoundedCornerShape(12.dp)
//        ) {
//            Text(text = "Proceed to Pay ₹${department?.fees ?: "-"}")
//        }
//    }
//}
//
//
//








//@Composable
//fun RegistrationScreen(
//    deptId: String,
//    navigateToPayment: () -> Unit,
//    viewModel: CreateTicketViewModel = hiltViewModel()
//) {
//    val context = LocalContext.current
//    val department = viewModel.department.collectAsState().value
//    val uiState by viewModel.uiState.collectAsState()
//
//    // Load department on change
//    LaunchedEffect(deptId) { viewModel.loadDepartment(deptId) }
//    viewModel.updateDeptId(deptId)
//
//    // Events (navigation / error)
//    LaunchedEffect(viewModel) {
//        viewModel.eventFlow.collect { event ->
//            when (event) {
//                is UiEvent.NavigateToPayment -> navigateToPayment()
//                is UiEvent.ShowError -> Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
//            }
//        }
//    }
//
//    Box(
//        modifier = Modifier
//            .fillMaxSize()
//            .background(Color(0xFFF6F9FB)) // soft hospital-like background
//            .padding(horizontal = 16.dp, vertical = 24.dp)
//    ) {
//        Column(
//            modifier = Modifier
//                .fillMaxWidth()
//                .verticalScroll(rememberScrollState()),
//            horizontalAlignment = Alignment.CenterHorizontally
//        ) {
//            Spacer(modifier = Modifier.height(7.dp))
//            // Title
//            Text(
//
//                text = "Patient Registration",
//                style = MaterialTheme.typography.headlineSmall.copy(
//                    fontWeight = FontWeight.Bold,
//                    color = Color(0xFF0277BD) // calm blue header
//                ),
//                modifier = Modifier.padding(bottom = 20.dp)
//            )
//
//            // Main card container
//            Card(
//                shape = RoundedCornerShape(20.dp),
//                elevation = CardDefaults.cardElevation(8.dp),
//                colors = CardDefaults.cardColors(containerColor = Color.White),
//                modifier = Modifier.fillMaxWidth()
//            ) {
//                Column(modifier = Modifier.padding(20.dp)) {
//
//                    // Department info card
//                    Card(
//                        shape = RoundedCornerShape(16.dp),
//                        elevation = CardDefaults.cardElevation(4.dp),
//                        colors = CardDefaults.cardColors(containerColor = Color(0xFFE3F2FD)), // light blue shade
//                        modifier = Modifier.fillMaxWidth()
//                    ) {
//                        Column(modifier = Modifier.padding(10.dp)) {
//                            Text(
//                                text = department?.name ?: "Loading...",
//                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
//                                color = Color(0xFF01579B)
//                            )
//                            Text("Doctor: ${department?.doctor ?: "-"}", color = Color(0xFF37474F))
//                            Spacer(modifier = Modifier.height(5.dp))
//                            Text(
//                                text = "Registration Fee: ₹${department?.fees ?: "--"}",
//                                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold),
//                                color = Color(0xFFD32F2F)
//                            )
//                        }
//                    }
//
//                    Spacer(modifier = Modifier.height(20.dp))
//
//                    // Input fields
//                    OutlinedTextField(
//                        value = uiState.name,
//                        onValueChange = { viewModel.updateName(it) },
//                        label = { Text("Patient Name") },
//                        leadingIcon = { Icon(Icons.Default.Person, contentDescription = null, tint = Color(0xFF0277BD)) },
//                        modifier = Modifier.fillMaxWidth(),
//                        shape = RoundedCornerShape(12.dp)
//                    )
//
//                    Spacer(modifier = Modifier.height(12.dp))
//
//                    OutlinedTextField(
//                        value = uiState.address,
//                        onValueChange = { viewModel.updateAddress(it) },
//                        label = { Text("Address") },
//                        leadingIcon = { Icon(Icons.Default.Home, contentDescription = null, tint = Color(0xFF0277BD)) },
//                        modifier = Modifier.fillMaxWidth(),
//                        shape = RoundedCornerShape(12.dp)
//                    )
//
//                    Spacer(modifier = Modifier.height(12.dp))
//
//                    OutlinedTextField(
//                        value = uiState.phone,
//                        onValueChange = { viewModel.updatePhone(it) },
//                        label = { Text("Contact Number") },
//                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
//                        leadingIcon = { Icon(Icons.Default.Call, contentDescription = null, tint = Color(0xFF0277BD)) },
//                        modifier = Modifier.fillMaxWidth(),
//                        shape = RoundedCornerShape(12.dp)
//                    )
//
//                    Spacer(modifier = Modifier.height(12.dp))
//
//                    OutlinedTextField(
//                        value = if (uiState.age == 0) "" else uiState.age.toString(),
//                        onValueChange = { viewModel.updateAge(it) },
//                        label = { Text("Age") },
//                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
//                        leadingIcon = { Icon(Icons.Default.Cake, contentDescription = null, tint = Color(0xFF0277BD)) },
//                        modifier = Modifier.fillMaxWidth(),
//                        shape = RoundedCornerShape(12.dp)
//                    )
//
//                    Spacer(modifier = Modifier.height(12.dp))
//
//                    GenderDropdown(
//                        selectedSex = uiState.sex,
//                        onSexSelected = { viewModel.updateSex(it) }
//                    )
//
//                    Spacer(modifier = Modifier.height(28.dp))
//
//                    // Proceed button
//                    Button(
//                        onClick = { viewModel.addFormFromState() },
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .height(56.dp),
//                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0277BD)),
//                        shape = RoundedCornerShape(16.dp)
//                    ) {
//                        Text(
//                            text = "Proceed to Pay ₹${department?.fees ?: "--"}",
//                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
//                            color = Color.White
//                        )
//                    }
//                }
//            }
//        }
//    }
//}
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun GenderDropdown(
//    selectedSex: Sex,
//    onSexSelected: (Sex) -> Unit
//) {
//    var expanded by remember { mutableStateOf(false) }
//
//    ExposedDropdownMenuBox(
//        expanded = expanded,
//        onExpandedChange = { expanded = !expanded }
//    ) {
//        OutlinedTextField(
//            value = selectedSex.name,
//            onValueChange = {},
//            readOnly = true,
//            label = { Text("Gender") },
//            modifier = Modifier
//                .menuAnchor()
//                .fillMaxWidth(),
//            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
//            colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(),
//            shape = RoundedCornerShape(12.dp)
//        )
//
//        ExposedDropdownMenu(
//            expanded = expanded,
//            onDismissRequest = { expanded = false }
//        ) {
//            Sex.entries.forEach { sex ->
//                DropdownMenuItem(
//                    text = { Text(sex.name) },
//                    onClick = {
//                        onSexSelected(sex)
//                        expanded = false
//                    }
//                )
//            }
//        }
//    }
//}
//

//@Preview(showBackground = true)
//@Composable
//fun RegistrationScreenPreview() {
//    RegistrationScreen(
//        deptId = "cardiology"
//
//    )
//}