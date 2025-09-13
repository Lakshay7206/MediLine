package com.example.mediline.superAdmin

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButtonDefaults.Icon
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.mediline.data.model.AdminProfile

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SuperAdminScreen(
    viewModel: SuperAdminViewModel = hiltViewModel(),
    onLogout: () -> Unit
) {
    val uiState by viewModel.superAdminUiState.collectAsState()
    val adminUiState by viewModel.adminUiState.collectAsState()
    var inviteEmail by remember { mutableStateOf("") }
    val snackbarHostState = remember { SnackbarHostState() }

    // State for dialog
    var adminToRemove by remember { mutableStateOf<AdminProfile?>(null) }

    // Show snackbar for invite success/error
    LaunchedEffect(adminUiState) {
        when (adminUiState) {
            is AdminUiState.Success -> {
                snackbarHostState.showSnackbar((adminUiState as AdminUiState.Success).message)
            }
            is AdminUiState.Error -> {
                snackbarHostState.showSnackbar((adminUiState as AdminUiState.Error).message)
            }
            else -> {}
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Super Admin Dashboard") },
                actions = {
                    TextButton(
                        onClick = onLogout,
                        colors = ButtonDefaults.textButtonColors(
                            contentColor = MaterialTheme.colorScheme.error
                        )
                    ) {
                        Icon(
                            Icons.Default.ExitToApp,
                            contentDescription = "Logout",
                            tint = MaterialTheme.colorScheme.error
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Logout", color = MaterialTheme.colorScheme.error)
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            /** Invite Admin Section **/
            Card(
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        "Invite New Admin",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                    OutlinedTextField(
                        value = inviteEmail,
                        onValueChange = { inviteEmail = it },
                        label = { Text("Admin Email") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Button(
                        onClick = {
                            if (inviteEmail.isNotBlank()) {
                                viewModel.sendInvite(inviteEmail)
                                inviteEmail = "" // clear input after sending
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                    ) {
                        Text("Send Invite", color = Color.White)
                    }
                }
            }

            /** Existing Admins Section **/
            Text(
                "Existing Admins",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )

            when {
                uiState.isLoading -> {
                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }

                uiState.error != null -> {
                    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = uiState.error!!,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(onClick = { viewModel.retryLoadAdmins() }) {
                            Text("Retry")
                        }
                    }
                }

                uiState.admins.isEmpty() -> {
                    Text("No admins found.", style = MaterialTheme.typography.bodyMedium)
                }

                else -> {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items(uiState.admins, key = { it.id }) { admin ->
                            Card(
                                shape = RoundedCornerShape(16.dp),
                                elevation = CardDefaults.cardElevation(6.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(
                                    modifier = Modifier.padding(20.dp),
                                    verticalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            Icons.Default.Person,
                                            contentDescription = null,
                                            tint = MaterialTheme.colorScheme.primary,
                                            modifier = Modifier.size(50.dp)
                                        )
                                        Spacer(modifier = Modifier.width(12.dp))
                                        Text(admin.name, style = MaterialTheme.typography.titleMedium)
                                    }
                                    Divider(color = Color.Gray.copy(alpha = 0.3f))
                                    Text("Email: ${admin.email}", style = MaterialTheme.typography.bodyMedium)
                                    Text("Role: ${admin.role}", style = MaterialTheme.typography.bodyMedium)

                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = "Remove Admin",
                                        color = MaterialTheme.colorScheme.error,
                                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                                        modifier = Modifier.clickable {
                                            adminToRemove = admin
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    /** Remove Confirmation Dialog **/
    if (adminToRemove != null) {
        AlertDialog(
            onDismissRequest = { adminToRemove = null },
            title = { Text("Remove Admin") },
            text = { Text("Are you sure you want to remove this admin?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        adminToRemove?.let { viewModel.removeAdmin(it) }
                        adminToRemove = null
                    }
                ) {
                    Text("Yes", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { adminToRemove = null }) {
                    Text("No")
                }
            }
        )
    }
}


//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun SuperAdminScreen(
//    viewModel: SuperAdminViewModel = hiltViewModel(),
//    onLogout: () -> Unit
//) {
//    val uiState by viewModel.uiState.collectAsState()
//    var inviteEmail by remember { mutableStateOf("") }
//    val snackbarHostState = remember { SnackbarHostState() }
//
//
//    Scaffold(
//        topBar = {
//            TopAppBar(
//                title = {
//                    Text(
//                        text = "Hospital Administrator", // Professional heading
//                        style = MaterialTheme.typography.titleLarge,
//                        color = MaterialTheme.colorScheme.onPrimary
//                    )
//                },
//                navigationIcon = {}, // No back button
//                actions = {}, // No logout
//                colors = TopAppBarDefaults.topAppBarColors(
//                    containerColor = MaterialTheme.colorScheme.primary
//                )
//            )
//
//        },
//        snackbarHost = { SnackbarHost(snackbarHostState) }
//    ) { padding ->
//        Column(
//            modifier = Modifier
//                .padding(padding)
//                .padding(16.dp)
//                .fillMaxSize(),
//            verticalArrangement = Arrangement.spacedBy(16.dp)
//        ) {
//
//            /** Invite Admin Section **/
//            Card(
//                shape = RoundedCornerShape(16.dp),
//                elevation = CardDefaults.cardElevation(8.dp),
//                modifier = Modifier.fillMaxWidth()
//            ) {
//                Column(
//                    modifier = Modifier.padding(20.dp),
//                    verticalArrangement = Arrangement.spacedBy(16.dp)
//                ) {
//                    Text(
//                        "Invite New Admin",
//                        style = MaterialTheme.typography.titleMedium,
//                        color = MaterialTheme.colorScheme.primary
//                    )
//                    OutlinedTextField(
//                        value = inviteEmail,
//                        onValueChange = { inviteEmail = it },
//                        label = { Text("Admin Email") },
//                        singleLine = true,
//                        modifier = Modifier.fillMaxWidth()
//                    )
//                    Button(
//                        onClick = { /* Invite logic */ },
//                        modifier = Modifier.fillMaxWidth(),
//                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
//                    ) {
//                        Text("Send Invite", color = Color.White)
//                    }
//                }
//            }
//
//            /** Existing Admins Section **/
//            Text(
//                "Existing Admins",
//                style = MaterialTheme.typography.titleMedium,
//                color = MaterialTheme.colorScheme.primary
//            )
//
//            when {
//                uiState.isLoading -> {
//                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
//                        CircularProgressIndicator()
//                    }
//                }
//
//                uiState.error != null -> {
//                    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
//                        Text(
//                            text = uiState.error!!,
//                            color = MaterialTheme.colorScheme.error,
//                            style = MaterialTheme.typography.bodyMedium
//                        )
//                        Spacer(modifier = Modifier.height(8.dp))
//                        Button(onClick = { viewModel.loadAllAdmins() }) {
//                            Text("Retry")
//                        }
//                    }
//                }
//
//                uiState.admins.isEmpty() -> {
//                    Text("No admins found.", style = MaterialTheme.typography.bodyMedium)
//                }
//
//                else -> {
//                    LazyColumn(
//                        verticalArrangement = Arrangement.spacedBy(12.dp),
//                        modifier = Modifier.fillMaxWidth()
//                    ) {
//                        items(uiState.admins) { admin ->
//                            Card(
//                                shape = RoundedCornerShape(16.dp),
//                                elevation = CardDefaults.cardElevation(6.dp),
//                                modifier = Modifier.fillMaxWidth()
//                            ) {
//                                Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
//                                    Row(verticalAlignment = Alignment.CenterVertically) {
//                                        Icon(Icons.Default.Person, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(50.dp))
//                                        Spacer(modifier = Modifier.width(12.dp))
//                                        Text(admin.name, style = MaterialTheme.typography.titleMedium)
//                                    }
//                                    Divider(color = Color.Gray.copy(alpha = 0.3f))
//                                    Text("Email: ${admin.email}", style = MaterialTheme.typography.bodyMedium)
//                                    Text("Role: ${admin.role}", style = MaterialTheme.typography.bodyMedium)
//                                }
//                            }
//                        }
//                    }
//                }
//            }
//        }
//    }
//}
//



//
//@Composable
//fun InviteAdminScreen(viewModel: AdminViewModel = hiltViewModel()) {
//    var email by remember { mutableStateOf("") }
//    val state by viewModel.uiState.collectAsState()
//
//    Column(Modifier.padding(16.dp)) {
//        TextField(value = email, onValueChange = { email = it }, label = { Text("Admin Email") })
//        Spacer(Modifier.height(16.dp))
//        Button(onClick = { viewModel.sendInvite(email) }) {
//            Text("Send Invite")
//        }
//
//        when (state) {
//            is AdminUiState.Loading -> Text("Sending invite...")
//            is AdminUiState.Success -> Text((state as AdminUiState.Success).message)
//            is AdminUiState.Error -> Text((state as AdminUiState.Error).message, color = Color.Red)
//            else -> {}
//        }
//    }
//}
//@Composable
//fun AcceptInviteScreen(
//    token: String,
//    viewModel: AdminViewModel = hiltViewModel()
//) {
//    var password by remember { mutableStateOf("") }
//    val state by viewModel.uiState.collectAsState()
//
//    Column(Modifier.padding(16.dp)) {
//        Text("You’ve been invited as an Admin")
//        Spacer(Modifier.height(8.dp))
//        TextField(value = password, onValueChange = { password = it }, label = { Text("Set Password") })
//        Spacer(Modifier.height(16.dp))
//        Button(onClick = { viewModel.acceptInvite(token, password) }) {
//            Text("Accept Invite")
//        }
//
//        when (state) {
//            is AdminUiState.Loading -> Text("Creating account...")
//            is AdminUiState.Success -> Text((state as AdminUiState.Success).message)
//            is AdminUiState.Error -> Text((state as AdminUiState.Error).message, color = Color.Red)
//            else -> {}
//        }
//    }
//}
