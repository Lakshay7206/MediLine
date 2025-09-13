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
import com.example.mediline.User.Screen
import com.example.mediline.User.ui.authentication.CurvedTopBar




@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)
@Composable
fun RegistrationScreen(
    deptId: String,
    navigateToPayment: (String) -> Unit,
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
                is UiEvent.NavigateToPayment -> {
                    navigateToPayment(event.formId)
                }
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
                        text = "Registration Fee: ₹100",
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
                    onValueChange = { viewModel.updateName(it)
                        viewModel.markFieldTouched("name")},
                    label = { Text("Patient Name") },
                    singleLine = true,
                    leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    isError = uiState.errors["name"] != null && uiState.touched["name"] == true
                )
                if (uiState.errors["name"] != null && uiState.touched["name"] == true) {
                    Text(
                        text = uiState.errors["name"]!!,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(start = 16.dp, top = 2.dp)
                    )
                }

                OutlinedTextField(
                    value = uiState.fatherName,
                    onValueChange = { viewModel.updateFatherName(it) },
                    label = { Text("Father's Name") },
                    singleLine = true,
                    leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    isError = uiState.errors["Father's Name"] != null
                )
                uiState.errors["Father's Name"]?.let { errorMsg ->
                    Text(
                        text = errorMsg,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(start = 16.dp, top = 2.dp)
                    )
                }

                OutlinedTextField(
                    value = uiState.address,
                    onValueChange = { viewModel.markFieldTouched("address")
                        viewModel.updateAddress(it)
                        },
                    label = { Text("Address") },
                    singleLine = true,
                    leadingIcon = { Icon(Icons.Default.Home, contentDescription = null) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    isError = uiState.errors["address"] != null && uiState.touched["address"] == true

                )
                if (uiState.errors["address"] != null && uiState.touched["address"] == true) {
                    Text(
                        text = uiState.errors["address"]!!,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(start = 16.dp, top = 2.dp)
                    )
                }

                OutlinedTextField(
                    value = uiState.phone,
                    onValueChange = {    viewModel.markFieldTouched("phone")
                        viewModel.updatePhone(it)
                                    },
                    label = { Text("Contact Number") },
                    singleLine = true,
                    leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    isError = uiState.errors["phone"] != null && uiState.touched["phone"] == true
                )
                if (uiState.errors["phone"] != null && uiState.touched["phone"] == true) {
                    Text(
                        text = uiState.errors["phone"]!!,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(start = 16.dp, top = 2.dp)
                    )
                }

                OutlinedTextField(
                    value = uiState.age,
                    onValueChange = { viewModel.updateAge(it)
                        viewModel.markFieldTouched("age")},
                    label = { Text("Age") },
                    singleLine = true,
                    leadingIcon = { Icon(Icons.Default.Cake, contentDescription = null) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    isError = uiState.errors["age"] != null&& uiState.touched["age"] == true
                )
                if (uiState.errors["age"] != null && uiState.touched["age"] == true) {
                    Text(
                        text = uiState.errors["age"]!!,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(start = 16.dp, top = 2.dp)
                    )
                }

                // Gender Dropdown
                ExposedDropdownMenuBox(
                    expanded = genderExpanded,
                    onExpandedChange = { genderExpanded = !genderExpanded }
                ) {
                    uiState.sex?.name?.let {
                        OutlinedTextField(
                            value = it,
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
                    }

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
                shape = RoundedCornerShape(12.dp),
                enabled = viewModel.validate()
            ) {
                Icon(Icons.Default.CheckCircle, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text(text = "Proceed to Payment")
            }
        }
    }
}

