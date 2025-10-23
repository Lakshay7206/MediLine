package com.example.mediline.superAdmin

import android.R
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Warning
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
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.mediline.User.ui.authentication.CurvedTopBar
import com.example.mediline.User.ui.theme.Black
import com.example.mediline.data.model.AdminProfile
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SuperAdminScreen(
    onLogout: () -> Unit,
    viewModel: SuperAdminViewModel = hiltViewModel()
) {
    val uiState by viewModel.superAdminUiState.collectAsState()
    val adminUiState by viewModel.adminUiState.collectAsState()
    var inviteEmail by remember { mutableStateOf("") }
    val snackbarHostState = remember { SnackbarHostState() }
    var adminToRemove by remember { mutableStateOf<AdminProfile?>(null) }

    // Snackbar for invite success/error
    LaunchedEffect(adminUiState) {
        when (adminUiState) {
            is AdminUiState.Success -> snackbarHostState.showSnackbar(
                (adminUiState as AdminUiState.Success).message,
                duration = SnackbarDuration.Short
            )
            is AdminUiState.Error -> snackbarHostState.showSnackbar(
                (adminUiState as AdminUiState.Error).message,
                duration = SnackbarDuration.Long
            )
            else -> {}
        }
    }

    Scaffold(
        topBar = {
            CurvedTopBar("Super Admin Dashboard", true) {}
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
                shape = RoundedCornerShape(24.dp),
                elevation = CardDefaults.cardElevation(16.dp),
                colors = CardDefaults.cardColors(containerColor =Color.White ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)

            ) {
                Column(
                    modifier = Modifier
                        .padding(24.dp),
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    // Title
                    Text(
                        "Invite New Admin",
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    )

                    // Description (Optional)
                    Text(
                        "Enter the email of the admin you want to invite. They will receive an invitation link.",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                        )
                    )

                    // Email Input
                    OutlinedTextField(
                        value = inviteEmail,
                        onValueChange = { inviteEmail = it },
                        label = { Text("Admin Email") },
                        singleLine = true,
                        leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
                        modifier = Modifier.fillMaxWidth(),


                    )

                    // Inside your Button composable:
                    Button(
                        onClick = {
                            if (inviteEmail.isNotBlank()) {
                                viewModel.sendInvite(inviteEmail)
                                // don’t clear email immediately; wait for success
                            }
                        },
                       // enabled = inviteEmail.isNotBlank() && adminUiState !is AdminUiState.Loading,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        shape = RoundedCornerShape(20.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                        elevation = ButtonDefaults.buttonElevation(defaultElevation = 8.dp)
                    ) {
                        if (adminUiState is AdminUiState.Loading) {
                            CircularProgressIndicator(
                                color = Color.White,
                                strokeWidth = 2.dp,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                "Sending Invite...",
                                color = Color.White,
                                style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.SemiBold)
                            )
                        } else {
                            Icon(Icons.Default.Send, contentDescription = null, tint = Color.White)
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                "Send Invite",
                                color = Color.White,
                                style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.SemiBold)
                            )
                        }
                    }


                    // Send Invite Button
//                    Button(
//                        onClick = {
//                            if (inviteEmail.isNotBlank()) {
//                                viewModel.sendInvite(inviteEmail)
//                                inviteEmail = ""
//                            }
//                        },
//                       // enabled = inviteEmail.isNotBlank(),
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .height(56.dp),
//                        shape = RoundedCornerShape(20.dp),
//                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
//                        elevation = ButtonDefaults.buttonElevation(defaultElevation = 8.dp)
//                    ) {
//                        Icon(Icons.Default.Send, contentDescription = null, tint = Color.White)
//                        Spacer(modifier = Modifier.width(10.dp))
//                        Text(
//                            "Send Invite",
//                            color = Color.White,
//                            style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.SemiBold)
//                        )
//                    }
               }
            }

            /** Existing Admins Section **/
            Text(
                "Existing Admins",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary
            )

            when {
                uiState.isLoading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                    }
                }

                uiState.error != null -> {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            Icons.Default.ErrorOutline,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.error,
                            modifier = Modifier.size(64.dp)
                        )
                        Spacer(Modifier.height(12.dp))
                        Text(
                            uiState.error!!,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodyMedium,
                            textAlign = TextAlign.Center
                        )
                        Spacer(Modifier.height(12.dp))
                        Button(onClick = { viewModel.retryLoadAdmins() }) { Text("Retry") }
                    }
                }

                uiState.admins.isEmpty() -> {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(Icons.Default.Group, contentDescription = null, modifier = Modifier.size(64.dp), tint = Color.Gray)
                            Spacer(Modifier.height(8.dp))
                            Text("No admins found.", style = MaterialTheme.typography.bodyMedium)
                        }
                    }
                }

                else -> {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(uiState.admins, key = { it.uid }) { admin ->
                            Card(
                                shape = RoundedCornerShape(20.dp),
                                elevation = CardDefaults.cardElevation(8.dp),
                                colors = CardDefaults.cardColors(containerColor = Color.White),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(20.dp),
                                    verticalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    // Row for avatar + info
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        // Avatar with safe random color
                                        val avatarColor = remember {
                                            Color(
                                                red = (100..200).random(),
                                                green = (150..230).random(),
                                                blue = (100..220).random()
                                            )
                                        }
                                        Box(
                                            modifier = Modifier
                                                .size(50.dp)
                                                .clip(CircleShape)
                                                .background(avatarColor),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                text = admin.email.first().uppercaseChar().toString(),
                                                color = Color.White,
                                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                                            )
                                        }

                                        Spacer(modifier = Modifier.width(16.dp))

                                        Column {
                                            Text(
                                                text = admin.email,
                                                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold),
                                                maxLines = 1,
                                                overflow = TextOverflow.Ellipsis
                                            )
                                            Text(
                                                text = admin.role,
                                                style = MaterialTheme.typography.bodyMedium,
                                                color = Color.Gray
                                            )
                                        }
                                    }

                                    // Remove button below info
                                    TextButton(
                                        onClick = { adminToRemove = admin },
                                        modifier = Modifier.align(Alignment.End),
                                        colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.error)
                                    ) {
                                        Icon(Icons.Default.Delete, contentDescription = "Remove Admin", tint = MaterialTheme.colorScheme.error)
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text("Remove", color = MaterialTheme.colorScheme.error)
                                    }
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
            text = { Text("Are you sure you want to remove ${adminToRemove?.email}? This action cannot be undone.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        adminToRemove?.let { viewModel.removeAdmin(it) }
                        adminToRemove = null
                    }
                ) { Text("Yes, Remove", color = MaterialTheme.colorScheme.error) }
            },
            dismissButton = {
                TextButton(onClick = { adminToRemove = null }) { Text("Cancel") }
            }
        )
    }
}

//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun SuperAdminScreen(
//    onLogout: () -> Unit,
//    viewModel: SuperAdminViewModel = hiltViewModel()
//) {
//    val uiState by viewModel.superAdminUiState.collectAsState()
//    val adminUiState by viewModel.adminUiState.collectAsState()
//    var inviteEmail by remember { mutableStateOf("") }
//    val snackbarHostState = remember { SnackbarHostState() }
//
//    // State for dialog
//    var adminToRemove by remember { mutableStateOf<AdminProfile?>(null) }
//
//    // Show snackbar for invite success/error
//    LaunchedEffect(adminUiState) {
//        when (adminUiState) {
//            is AdminUiState.Success -> {
//                snackbarHostState.showSnackbar((adminUiState as AdminUiState.Success).message)
//            }
//            is AdminUiState.Error -> {
//                snackbarHostState.showSnackbar((adminUiState as AdminUiState.Error).message)
//            }
//            else -> {}
//        }
//    }
//
//    Scaffold(
//        topBar = {
//            TopAppBar(
//                title = { Text("Super Admin Dashboard") },
//                actions = {
//                    TextButton(
//                        onClick = onLogout,
//                        colors = ButtonDefaults.textButtonColors(
//                            contentColor = MaterialTheme.colorScheme.error
//                        )
//                    ) {
//                        Icon(
//                            Icons.Default.ExitToApp,
//                            contentDescription = "Logout",
//                            tint = MaterialTheme.colorScheme.error
//                        )
//                        Spacer(modifier = Modifier.width(4.dp))
//                        Text("Logout", color = MaterialTheme.colorScheme.error)
//                    }
//                }
//            )
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
//                        onClick = {
//                            if (inviteEmail.isNotBlank()) {
//                                viewModel.sendInvite(inviteEmail)
//                                inviteEmail = "" // clear input after sending
//                            }
//                        },
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
//                        Button(onClick = { viewModel.retryLoadAdmins() }) {
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
//                        items(uiState.admins, key = { it.uid }) { admin ->
//                            Card(
//                                shape = RoundedCornerShape(16.dp),
//                                elevation = CardDefaults.cardElevation(6.dp),
//                                modifier = Modifier.fillMaxWidth()
//                            ) {
//                                Column(
//                                    modifier = Modifier.padding(20.dp),
//                                    verticalArrangement = Arrangement.spacedBy(8.dp)
//                                ) {
//                                    Row(verticalAlignment = Alignment.CenterVertically) {
//                                        Icon(
//                                            Icons.Default.Person,
//                                            contentDescription = null,
//                                            tint = MaterialTheme.colorScheme.primary,
//                                            modifier = Modifier.size(50.dp)
//                                        )
//                                        Spacer(modifier = Modifier.width(12.dp))
//                                        Text(admin.email, style = MaterialTheme.typography.titleMedium)
//                                    }
//                                    Divider(color = Color.Gray.copy(alpha = 0.3f))
//                                    Text("Email: ${admin.email}", style = MaterialTheme.typography.bodyMedium)
//                                    Text("Role: ${admin.role}", style = MaterialTheme.typography.bodyMedium)
//
//                                    Spacer(modifier = Modifier.height(8.dp))
//                                    Text(
//                                        text = "Remove Admin",
//                                        color = MaterialTheme.colorScheme.error,
//                                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
//                                        modifier = Modifier.clickable {
//                                            adminToRemove = admin
//                                        }
//                                    )
//                                }
//                            }
//                        }
//                    }
//                }
//            }
//        }
//    }
//
//    /** Remove Confirmation Dialog **/
//    if (adminToRemove != null) {
//        AlertDialog(
//            onDismissRequest = { adminToRemove = null },
//            title = { Text("Remove Admin") },
//            text = { Text("Are you sure you want to remove this admin?") },
//            confirmButton = {
//                TextButton(
//                    onClick = {
//                        adminToRemove?.let { viewModel.removeAdmin(it) }
//                        adminToRemove = null
//                    }
//                ) {
//                    Text("Yes", color = MaterialTheme.colorScheme.error)
//                }
//            },
//            dismissButton = {
//                TextButton(onClick = { adminToRemove = null }) {
//                    Text("No")
//                }
//            }
//        )
//    }
//}


