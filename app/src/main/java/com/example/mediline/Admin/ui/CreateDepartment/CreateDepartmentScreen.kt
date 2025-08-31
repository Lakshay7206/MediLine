package com.example.mediline.Admin.ui.CreateDepartment

import androidx.compose.ui.geometry.isEmpty
import androidx.compose.ui.semantics.error
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.mediline.data.room.DepartmentEntity
import com.example.mediline.ui.admin.department.AdminDepartmentScreenState
import com.example.mediline.ui.admin.department.AdminDepartmentViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminDepartmentScreen(
    viewModel: AdminDepartmentViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(key1 = uiState.error) {
        uiState.error?.let {
            snackbarHostState.showSnackbar(
                message = it,
                duration = SnackbarDuration.Short
            )
            // Consider clearing the error in VM after showing
        }
    }
    LaunchedEffect(key1 = uiState.successMessage) {
        uiState.successMessage?.let {
            snackbarHostState.showSnackbar(
                message = it,
                duration = SnackbarDuration.Short
            )
            // Consider clearing the success message in VM after showing
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text("Manage Departments") },
                actions = {
                    IconButton(onClick = { viewModel.triggerSyncWithFirestore() }) {
                        Icon(Icons.Filled.Refresh, contentDescription = "Sync Departments")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { viewModel.showCreateDepartmentForm() }) {
                Icon(Icons.Filled.Add, contentDescription = "Add Department")
            }
        }
    ) { paddingValues ->
        Box(modifier = Modifier
            .padding(paddingValues)
            .fillMaxSize()) {
            if (uiState.isLoading && uiState.departments.isEmpty()) { // Show loading only if list is empty
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else if (uiState.departments.isEmpty()) {
                Text("No departments found.", modifier = Modifier.align(Alignment.Center))
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(uiState.departments, key = { it.id }) { department ->
                        DepartmentItem(
                            department = department,
                            onEdit = { viewModel.loadDepartmentForEdit(department) },
                            onDelete = { viewModel.deleteDepartment(department.id) }
                        )
                    }
                }
            }

            if (uiState.isFormVisible) {
                DepartmentFormDialog(
                    uiState = uiState,
                    onNameChange = viewModel::onDepartmentNameChanged,
                    onDescriptionChange = viewModel::onDepartmentDescriptionChanged,
                    onDoctorChange = viewModel::onDepartmentDoctorChanged,
                    onFeesChange = viewModel::onDepartmentFeesChanged,
                    onSave = viewModel::saveDepartment,
                    onDismiss = viewModel::dismissForm
                )
            }
            // Global loading indicator for actions like save/delete/sync
            if (uiState.isLoading && !uiState.departments.isEmpty()) {
                Box(modifier = Modifier
                    .fillMaxSize()
                    .align(Alignment.Center)) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
            }
        }
    }
}

@Composable
fun DepartmentItem(
    department: DepartmentEntity,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Card(elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)) {
        Column(modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()) {
            Text(department.name, style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(4.dp))
            Text("Description: ${department.description}", style = MaterialTheme.typography.bodyMedium)
            Text("Doctor: ${department.doctor}", style = MaterialTheme.typography.bodyMedium)
            Text("Fees: ${department.fees}", style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth()) {
                IconButton(onClick = onEdit) {
                    Icon(Icons.Filled.Edit, contentDescription = "Edit")
                }
                IconButton(onClick = onDelete) {
                    Icon(Icons.Filled.Delete, contentDescription = "Delete")
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DepartmentFormDialog(
    uiState: AdminDepartmentScreenState,
    onNameChange: (String) -> Unit,
    onDescriptionChange: (String) -> Unit,
    onDoctorChange: (String) -> Unit,
    onFeesChange: (String) -> Unit,
    onSave: () -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(modifier = Modifier.padding(16.dp)) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = if (uiState.currentEditDepartmentId == null) "Create Department" else "Edit Department",
                    style = MaterialTheme.typography.titleLarge
                )

                OutlinedTextField(
                    value = uiState.departmentNameInput,
                    onValueChange = onNameChange,
                    label = { Text("Department Name") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                OutlinedTextField(
                    value = uiState.departmentDescriptionInput,
                    onValueChange = onDescriptionChange,
                    label = { Text("Description") },
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 3
                )
                OutlinedTextField(
                    value = uiState.departmentDoctorInput,
                    onValueChange = onDoctorChange,
                    label = { Text("Doctor") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                OutlinedTextField(
                    value = uiState.departmentFeesInput,
                    onValueChange = onFeesChange,
                    label = { Text("Fees") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("Cancel")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(onClick = onSave, enabled = !uiState.isLoading) {
                        if (uiState.isLoading && uiState.isFormVisible) { // Show loading only on form save
                            CircularProgressIndicator(modifier = Modifier.size(20.dp), strokeWidth = 2.dp, color = MaterialTheme.colorScheme.onPrimary)
                        } else {
                            Text("Save")
                        }
                    }
                }
                uiState.error?.let {
                    if(uiState.isFormVisible){ // Show form specific error
                        Text(it, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
                    }
                }
            }
        }
    }
}
