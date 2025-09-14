package com.example.mediline.Admin.ui.CreateDepartment

import android.widget.Toast
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.ui.geometry.isEmpty
import androidx.compose.ui.semantics.error
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.mediline.Admin.ui.Screen
import com.example.mediline.Admin.ui.home.BottomNavBar
import com.example.mediline.data.model.Form
import com.example.mediline.data.room.DepartmentEntity
import com.example.mediline.ui.admin.department.AdminDepartmentScreenState
import com.example.mediline.ui.admin.department.AdminDepartmentViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminDepartmentScreen(
    onHome: () -> Unit,
    onCreateTicket: () -> Unit,
    onProfile: () -> Unit,
    viewModel: AdminDepartmentViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(uiState.successMessage) {
        uiState.successMessage?.let {
            snackbarHostState.showSnackbar(it, duration = SnackbarDuration.Short)
            viewModel.clearSuccessMessage()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
//            CurvedTopBar(
//                title = "Departments",
//                onRefreshClick = { viewModel.triggerSyncWithFirestore(true) }
//            )
            CurvedTopBarRefresh("Departments",{viewModel.triggerSyncWithFirestore(true)})
        },
        bottomBar = {
            BottomNavBar { route ->
                when (route) {
                    Screen.Home.route -> onHome()
                    Screen.CreateTicket.route -> onCreateTicket()
                    Screen.Departments.route -> {}
                    Screen.Profile.route -> onProfile()
                }
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { viewModel.showCreateDepartmentForm() },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                shape = RoundedCornerShape(16.dp)
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Add Department")
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            when {
                uiState.isLoading && uiState.departments.isEmpty() -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }

                uiState.departments.isEmpty() -> {
                    Text(
                        "No departments found.",
                        style = MaterialTheme.typography.bodyLarge.copy(
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        ),
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
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

            if (uiState.isLoading && uiState.departments.isNotEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.4f))
                        .align(Alignment.Center)
                ) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
            }
        }
    }
}

@Composable
fun CurvedTopBar(title: String, onRefreshClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.primary, shape = RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp))
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            )
            IconButton(onClick = onRefreshClick) {
                Icon(Icons.Filled.Refresh, contentDescription = "Sync", tint = MaterialTheme.colorScheme.onPrimary)
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
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                department.name,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold)
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text("Description: ${department.description}", style = MaterialTheme.typography.bodyMedium)
            Text("Doctor: ${department.doctor}", style = MaterialTheme.typography.bodyMedium)
            Text("Fees: ${department.fees}", style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                horizontalArrangement = Arrangement.End,
                modifier = Modifier.fillMaxWidth()
            ) {
                IconButton(onClick = onEdit) {
                    Icon(Icons.Filled.Edit, contentDescription = "Edit", tint = MaterialTheme.colorScheme.primary)
                }
                IconButton(onClick = onDelete) {
                    Icon(Icons.Filled.Delete, contentDescription = "Delete", tint = MaterialTheme.colorScheme.error)
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
        Card(
            shape = RoundedCornerShape(24.dp),
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 10.dp)
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = if (uiState.currentEditDepartmentId == null) "Create Department" else "Edit Department",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.SemiBold)
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
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) { Text("Cancel") }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = onSave,
                        enabled = !uiState.isLoading,
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        if (uiState.isLoading && uiState.isFormVisible) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(20.dp),
                                strokeWidth = 2.dp,
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                        } else {
                            Text("Save")
                        }
                    }
                }

                uiState.error?.let {
                    if (uiState.isFormVisible) {
                        Text(
                            it,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }
        }
    }
}


@Composable
fun CurvedTopBar(
    title: String,
    enabled: Boolean,
    navigateBack: (() -> Unit)? = null
) {
    val primary = MaterialTheme.colorScheme.primary
    val onPrimary = MaterialTheme.colorScheme.onPrimary

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp),

        ) {
        Canvas(modifier = Modifier.matchParentSize()) {
            val width = size.width
            val height = size.height

            val path = Path().apply {
                moveTo(0f, height * 0.6f)
                quadraticBezierTo(
                    width / 2, height, // control point
                    width, height * 0.6f // end point
                )
                lineTo(width, 0f)
                lineTo(0f, 0f)
                close()
            }

            drawPath(
                path = path,
                color = primary
            )
        }
        if(enabled){
            IconButton(
                onClick = { navigateBack?.invoke() },
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(16.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = onPrimary
                )
            }
        }
        Text(
            text = title,
            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
            color = onPrimary,
            modifier = Modifier
                .align(Alignment.Center)
        )
    }
}


@Composable
fun CurvedTopBarRefresh(
    title: String,
    onRefreshClick: () -> Unit
) {
    val primary = MaterialTheme.colorScheme.primary
    val onPrimary = MaterialTheme.colorScheme.onPrimary

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp),

        ) {
        Canvas(modifier = Modifier.matchParentSize()) {
            val width = size.width
            val height = size.height

            val path = Path().apply {
                moveTo(0f, height * 0.6f)
                quadraticBezierTo(
                    width / 2, height, // control point
                    width, height * 0.6f // end point
                )
                lineTo(width, 0f)
                lineTo(0f, 0f)
                close()
            }

            drawPath(
                path = path,
                color = primary
            )
        }

            IconButton(
                onClick = {onRefreshClick},
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(16.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = "Refresh",
                    tint = onPrimary
                )
            }

        Text(
            text = title,
            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
            color = onPrimary,
            modifier = Modifier
                .align(Alignment.Center)
        )
    }
}

//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun AdminDepartmentScreen(
//
//    onHome: () -> Unit,
//    onCreateTicket: () -> Unit,
//    onProfile:()->Unit,
//    viewModel: AdminDepartmentViewModel = hiltViewModel(),
//) {
//    val uiState by viewModel.uiState.collectAsState()
//    val snackbarHostState = remember { SnackbarHostState() }
//
//    LaunchedEffect(uiState.successMessage) {
//        uiState.successMessage?.let {
//            snackbarHostState.showSnackbar(it, duration = SnackbarDuration.Short)
//            viewModel.clearSuccessMessage() // clear so it doesn’t repeat
//        }
//    }
//
//
//
//
//    Scaffold(
//        snackbarHost = { SnackbarHost(snackbarHostState) },
//        topBar = {
//            TopAppBar(
//                title = {
//                    Row(
//                        modifier = Modifier.fillMaxWidth(),
//                        verticalAlignment = Alignment.CenterVertically,
//                        horizontalArrangement = Arrangement.SpaceBetween
//                    ) {
//                        Text(
//                            "Departments",
//                            style = MaterialTheme.typography.titleLarge.copy(
//                                fontWeight = FontWeight.SemiBold,
//                                color = MaterialTheme.colorScheme.onSurface
//                            )
//                        )
//                        IconButton(onClick = { viewModel.triggerSyncWithFirestore(fromUserAction = true) }) {
//                            Icon(
//                                Icons.Filled.Refresh,
//                                contentDescription = "Sync Departments",
//                                tint = MaterialTheme.colorScheme.primary
//                            )
//                        }
//                    }
//                },
//                colors = TopAppBarDefaults.topAppBarColors(
//                    containerColor = MaterialTheme.colorScheme.surface,
//                    titleContentColor = MaterialTheme.colorScheme.onSurface
//                )
//            )
//
//        },
//        bottomBar = {
//            BottomNavBar { route ->
//                when (route) {
//                    Screen.Home.route -> onHome()
//                    Screen.CreateTicket.route -> onCreateTicket()
//                    Screen.Departments.route -> {} // already here
//                    Screen.Profile.route -> onProfile()
//                }
//            }
//        }
//        ,
//        floatingActionButton = {
//            FloatingActionButton(
//                onClick = { viewModel.showCreateDepartmentForm() },
//                containerColor = MaterialTheme.colorScheme.primary,
//                contentColor = MaterialTheme.colorScheme.onPrimary,
//                shape = RoundedCornerShape(16.dp),
//                elevation = FloatingActionButtonDefaults.elevation(defaultElevation = 6.dp)
//            ) {
//                Icon(Icons.Filled.Add, contentDescription = "Add Department")
//            }
//        }
//    ) { paddingValues ->
//        Box(
//            modifier = Modifier
//                .padding(paddingValues)
//                .fillMaxSize()
//                .background(MaterialTheme.colorScheme.background)
//        ) {
//            when {
//                uiState.isLoading && uiState.departments.isEmpty() -> {
//                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
//                }
//
//                uiState.departments.isEmpty() -> {
//                    Text(
//                        "No departments found.",
//                        style = MaterialTheme.typography.bodyLarge.copy(
//                            color = MaterialTheme.colorScheme.onSurfaceVariant
//                        ),
//                        modifier = Modifier.align(Alignment.Center)
//                    )
//                }
//
//                else -> {
//                    LazyColumn(
//                        modifier = Modifier.fillMaxSize(),
//                        contentPadding = PaddingValues(16.dp),
//                        verticalArrangement = Arrangement.spacedBy(16.dp)
//                    ) {
//                        items(uiState.departments, key = { it.id }) { department ->
//                            // Yaha tera DepartmentItem call already hai
//                            DepartmentItem(
//                                department = department,
//                                onEdit = { viewModel.loadDepartmentForEdit(department) },
//                                onDelete = { viewModel.deleteDepartment(department.id) }
//                            )
//                        }
//                    }
//                }
//            }
//
//            if (uiState.isFormVisible) {
//                // Tera DepartmentFormDialog call already hai
//                DepartmentFormDialog(
//                    uiState = uiState,
//                    onNameChange = viewModel::onDepartmentNameChanged,
//                    onDescriptionChange = viewModel::onDepartmentDescriptionChanged,
//                    onDoctorChange = viewModel::onDepartmentDoctorChanged,
//                    onFeesChange = viewModel::onDepartmentFeesChanged,
//                    onSave = viewModel::saveDepartment,
//                    onDismiss = viewModel::dismissForm
//                )
//            }
//
//            if (uiState.isLoading && uiState.departments.isNotEmpty()) {
//                Box(
//                    modifier = Modifier
//                        .fillMaxSize()
//                        .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.4f))
//                        .align(Alignment.Center)
//                ) {
//                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
//                }
//            }
//        }
//    }
//}
//
//
//@Composable
//fun DepartmentItem(
//    department: DepartmentEntity,
//    onEdit: () -> Unit,
//    onDelete: () -> Unit
//) {
//    Card(
//        modifier = Modifier.fillMaxWidth(),
//        shape = RoundedCornerShape(16.dp),
//        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
//        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
//    ) {
//        Column(modifier = Modifier.padding(16.dp)) {
//            Text(
//                department.name,
//                style = MaterialTheme.typography.titleMedium.copy(
//                    fontWeight = FontWeight.SemiBold
//                )
//            )
//            Spacer(modifier = Modifier.height(6.dp))
//            Text("Description: ${department.description}", style = MaterialTheme.typography.bodyMedium)
//            Text("Doctor: ${department.doctor}", style = MaterialTheme.typography.bodyMedium)
//            Text("Fees: ${department.fees}", style = MaterialTheme.typography.bodyMedium)
//            Spacer(modifier = Modifier.height(12.dp))
//            Row(
//                horizontalArrangement = Arrangement.End,
//                modifier = Modifier.fillMaxWidth()
//            ) {
//                IconButton(onClick = onEdit) {
//                    Icon(Icons.Filled.Edit, contentDescription = "Edit", tint = MaterialTheme.colorScheme.primary)
//                }
//                IconButton(onClick = onDelete) {
//                    Icon(Icons.Filled.Delete, contentDescription = "Delete", tint = MaterialTheme.colorScheme.error)
//                }
//            }
//        }
//    }
//}
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun DepartmentFormDialog(
//    uiState: AdminDepartmentScreenState,
//    onNameChange: (String) -> Unit,
//    onDescriptionChange: (String) -> Unit,
//    onDoctorChange: (String) -> Unit,
//    onFeesChange: (String) -> Unit,
//    onSave: () -> Unit,
//    onDismiss: () -> Unit
//) {
//    Dialog(onDismissRequest = onDismiss) {
//        Card(
//            shape = RoundedCornerShape(20.dp),
//            modifier = Modifier.fillMaxWidth(),
//            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
//        ) {
//            Column(
//                modifier = Modifier.padding(20.dp),
//                verticalArrangement = Arrangement.spacedBy(16.dp)
//            ) {
//                Text(
//                    text = if (uiState.currentEditDepartmentId == null) "Create Department" else "Edit Department",
//                    style = MaterialTheme.typography.titleLarge.copy(
//                        fontWeight = FontWeight.SemiBold
//                    )
//                )
//
//                OutlinedTextField(
//                    value = uiState.departmentNameInput,
//                    onValueChange = onNameChange,
//                    label = { Text("Department Name") },
//                    modifier = Modifier.fillMaxWidth(),
//                    singleLine = true
//                )
//                OutlinedTextField(
//                    value = uiState.departmentDescriptionInput,
//                    onValueChange = onDescriptionChange,
//                    label = { Text("Description") },
//                    modifier = Modifier.fillMaxWidth(),
//                    maxLines = 3
//                )
//                OutlinedTextField(
//                    value = uiState.departmentDoctorInput,
//                    onValueChange = onDoctorChange,
//                    label = { Text("Doctor") },
//                    modifier = Modifier.fillMaxWidth(),
//                    singleLine = true
//                )
//                OutlinedTextField(
//                    value = uiState.departmentFeesInput,
//                    onValueChange = onFeesChange,
//                    label = { Text("Fees") },
//                    modifier = Modifier.fillMaxWidth(),
//                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
//                    singleLine = true
//                )
//
//                Row(
//                    modifier = Modifier.fillMaxWidth(),
//                    horizontalArrangement = Arrangement.End,
//                    verticalAlignment = Alignment.CenterVertically
//                ) {
//                    TextButton(onClick = onDismiss) {
//                        Text("Cancel")
//                    }
//                    Spacer(modifier = Modifier.width(8.dp))
//                    Button(
//                        onClick = onSave,
//                        enabled = !uiState.isLoading,
//                        shape = RoundedCornerShape(12.dp)
//                    ) {
//                        if (uiState.isLoading && uiState.isFormVisible) {
//                            CircularProgressIndicator(
//                                modifier = Modifier.size(20.dp),
//                                strokeWidth = 2.dp,
//                                color = MaterialTheme.colorScheme.onPrimary
//                            )
//                        } else {
//                            Text("Save")
//                        }
//                    }
//                }
//
//                uiState.error?.let {
//                    if (uiState.isFormVisible) {
//                        Text(
//                            it,
//                            color = MaterialTheme.colorScheme.error,
//                            style = MaterialTheme.typography.bodySmall
//                        )
//                    }
//                }
//            }
//        }
//    }
//}
//
//

