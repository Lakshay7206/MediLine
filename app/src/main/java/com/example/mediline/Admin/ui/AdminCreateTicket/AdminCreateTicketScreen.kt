package com.example.mediline.Admin.ui.AdminCreateTicket
import android.R.id.primary
import androidx.compose.foundation.Canvas
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Bloodtype
import androidx.compose.material.icons.filled.Cake
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Wc
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.mediline.User.ui.createTicket.Sex
import com.example.mediline.User.ui.theme.PrimaryGreen
import kotlin.io.path.Path
import kotlin.io.path.moveTo


// Form Content

//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun AdminCreateTicketScreen(
//    viewModel: AdminCreateTicketViewModel = hiltViewModel()
//) {
//    val uiState by viewModel.uiState.collectAsState()
//
//    val bloodGroups = listOf("A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-")
//
//    var sexExpanded by remember { mutableStateOf(false) }
//    var bloodGroupExpanded by remember { mutableStateOf(false) }
//
//    Scaffold(
//        topBar = {
//            TopAppBar(
//                title = { Text("🩺 Admin Panel") },
//                colors = TopAppBarDefaults.topAppBarColors(
//                    containerColor = MaterialTheme.colorScheme.primary,
//                    titleContentColor = MaterialTheme.colorScheme.onPrimary
//                )
//            )
//        },
//        bottomBar = {
//            Text(
//                text = "© 2025 Your Hospital Admin Panel",
//                style = MaterialTheme.typography.bodySmall,
//                color = MaterialTheme.colorScheme.onSurfaceVariant,
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(12.dp),
//                textAlign = TextAlign.Center
//            )
//        }
//    ) { innerPadding ->
//
//        Column(
//            modifier = Modifier
//                .fillMaxSize()
//                .background(MaterialTheme.colorScheme.background) // ✅ stays white/light
//                .padding(innerPadding)
//                .verticalScroll(rememberScrollState())
//                .padding(20.dp),
//            verticalArrangement = Arrangement.spacedBy(24.dp)
//        ) {
//            // Page Title
//            Text(
//                text = "Create New Ticket  ",
//                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
//                color = MaterialTheme.colorScheme.onSurface,
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(top = 20.dp), // 👈 push it down a little
//                textAlign = TextAlign.Center
//            )
//
//
//
//            // Card wrapping form
//
//                Column(
//                    modifier = Modifier.padding(20.dp),
//                    verticalArrangement = Arrangement.spacedBy(16.dp)
//                ) {
//                    // Name
//                    OutlinedTextField(
//                        value = uiState.name,
//                        onValueChange = { viewModel.onNameChange(it) },
//                        label = { Text("Name") },
//                        singleLine = true,
//                        leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
//                        modifier = Modifier.fillMaxWidth(),
//                        shape = RoundedCornerShape(12.dp)
//                    )
//
//                    // Mobile Number
//                    OutlinedTextField(
//                        value = uiState.phone,
//                        onValueChange = { viewModel.onPhoneChange(it) },
//                        label = { Text("Mobile Number") },
//                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
//                        singleLine = true,
//                        leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null) },
//                        modifier = Modifier.fillMaxWidth(),
//                        shape = RoundedCornerShape(12.dp)
//                    )
//
//                    // Age
//                    OutlinedTextField(
//                        value = uiState.age,
//                        onValueChange = { viewModel.onAgeChange(it) },
//                        label = { Text("Age") },
//                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
//                        singleLine = true,
//                        leadingIcon = { Icon(Icons.Default.Cake, contentDescription = null) },
//                        modifier = Modifier.fillMaxWidth(),
//                        shape = RoundedCornerShape(12.dp)
//                    )
//
//                    // Sex Dropdown
//                    ExposedDropdownMenuBox(
//                        expanded = sexExpanded,
//                        onExpandedChange = { sexExpanded = !sexExpanded }
//                    ) {
//                        OutlinedTextField(
//                            value = uiState.sex?.name ?: "",
//                            onValueChange = {},
//                            label = { Text("Sex") },
//                            readOnly = true,
//                            leadingIcon = { Icon(Icons.Default.Wc, contentDescription = null) },
//                            trailingIcon = {
//                                ExposedDropdownMenuDefaults.TrailingIcon(expanded = sexExpanded)
//                            },
//                            modifier = Modifier
//                                .fillMaxWidth()
//                                .menuAnchor(),
//                            shape = RoundedCornerShape(12.dp)
//                        )
//
//                        ExposedDropdownMenu(
//                            expanded = sexExpanded,
//                            onDismissRequest = { sexExpanded = false }
//                        ) {
//                            Sex.entries.forEach { sex ->
//                                DropdownMenuItem(
//                                    text = { Text(sex.name) },
//                                    onClick = {
//                                        viewModel.onSexChange(sex.name)
//                                        sexExpanded = false
//                                    }
//                                )
//                            }
//                        }
//                    }
//
//                    // Address
//                    OutlinedTextField(
//                        value = uiState.address,
//                        onValueChange = { viewModel.onAddressChange(it) },
//                        label = { Text("Address") },
//                        leadingIcon = { Icon(Icons.Default.Home, contentDescription = null) },
//                        modifier = Modifier.fillMaxWidth(),
//                        shape = RoundedCornerShape(12.dp)
//                    )
//
//                    // Blood Group Dropdown
//                    ExposedDropdownMenuBox(
//                        expanded = bloodGroupExpanded,
//                        onExpandedChange = { bloodGroupExpanded = !bloodGroupExpanded }
//                    ) {
//                        OutlinedTextField(
//                            value = uiState.bloodGroup ?: "",
//                            onValueChange = {},
//                            label = { Text("Blood Group (Optional)") },
//                            readOnly = true,
//                            leadingIcon = { Icon(Icons.Default.Favorite, contentDescription = null) },
//                            trailingIcon = {
//                                ExposedDropdownMenuDefaults.TrailingIcon(expanded = bloodGroupExpanded)
//                            },
//                            modifier = Modifier
//                                .fillMaxWidth()
//                                .menuAnchor(type = MenuAnchorType.PrimaryNotEditable),
//                            shape = RoundedCornerShape(12.dp)
//                        )
//
//                        ExposedDropdownMenu(
//                            expanded = bloodGroupExpanded,
//                            onDismissRequest = { bloodGroupExpanded = false }
//                        ) {
//                            bloodGroups.forEach { bg ->
//                                DropdownMenuItem(
//                                    text = { Text(bg) },
//                                    onClick = {
//                                        viewModel.onBloodGroupChange(bg)
//                                        bloodGroupExpanded = false
//                                    }
//                                )
//                            }
//                        }
//
//                }
//            }
//
//            // Create Ticket Button
//            Button(
//                onClick = { viewModel.submitTicket(userId = "1") },
//                enabled = !uiState.isSubmitting,
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .height(56.dp),
//                shape = RoundedCornerShape(14.dp)
//            ) {
//                Icon(Icons.Default.CheckCircle, contentDescription = null)
//                Spacer(Modifier.width(8.dp))
//                Text(
//                    if (uiState.isSubmitting) "Creating..." else "Create Ticket",
//                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
//                )
//            }
//
//            // Error / Success Messages
//            uiState.errorMessage?.let {
//                Text(
//                    it,
//                    color = Color.Red,
//                    style = MaterialTheme.typography.bodyMedium
//                )
//            }
//        }
//    }
//}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminCreateTicketScreen(
    viewModel: AdminCreateTicketViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    val bloodGroups = listOf("A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-")

    var sexExpanded by remember { mutableStateOf(false) }
    var bloodGroupExpanded by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            CurvedTopBar()
        },
        bottomBar = {
            Text(
                text = "© 2025 Your Hospital Admin Panel",
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
                .background(MaterialTheme.colorScheme.background) // ✅ White / Theme BG
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // Title
            Text(
                text = "Create New Ticket  ",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onSurface,
                modifier= Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
            )

            // Card wrapping form
//            Card(
//                modifier = Modifier.fillMaxWidth(),
//                shape = RoundedCornerShape(16.dp),
//                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
//                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
//            ) {
                Column(
                    modifier = Modifier
                        .padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Name
                    OutlinedTextField(
                        value = uiState.name,
                        onValueChange = { viewModel.onNameChange(it) },
                        label = { Text("Name") },
                        singleLine = true,
                        leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )

                    // Mobile Number
                    OutlinedTextField(
                        value = uiState.phone,
                        onValueChange = { viewModel.onPhoneChange(it) },
                        label = { Text("Mobile Number") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                        singleLine = true,
                        leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )

                    // Age
                    OutlinedTextField(
                        value = uiState.age,
                        onValueChange = { viewModel.onAgeChange(it) },
                        label = { Text("Age") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        leadingIcon = { Icon(Icons.Default.Cake, contentDescription = null) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )

                    // Sex Dropdown
                    ExposedDropdownMenuBox(
                        expanded = sexExpanded,
                        onExpandedChange = { sexExpanded = !sexExpanded }
                    ) {
                        OutlinedTextField(
                            value = uiState.sex?.name ?: "",
                            onValueChange = {},
                            label = { Text("Sex") },
                            readOnly = true,
                            leadingIcon = { Icon(Icons.Default.Wc, contentDescription = null) },
                            trailingIcon = {
                                ExposedDropdownMenuDefaults.TrailingIcon(expanded = sexExpanded)
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .menuAnchor(),
                            shape = RoundedCornerShape(12.dp)
                        )

                        ExposedDropdownMenu(
                            expanded = sexExpanded,
                            onDismissRequest = { sexExpanded = false }
                        ) {
                            Sex.entries.forEach { sex ->
                                DropdownMenuItem(
                                    text = { Text(sex.name) },
                                    onClick = {
                                        viewModel.onSexChange(sex.name)
                                        sexExpanded = false
                                    }
                                )
                            }
                        }
                    }

                    // Address
                    OutlinedTextField(
                        value = uiState.address,
                        onValueChange = { viewModel.onAddressChange(it) },
                        label = { Text("Address") },
                        leadingIcon = { Icon(Icons.Default.Home, contentDescription = null) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )

                    // Blood Group Dropdown
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
                                .menuAnchor(type = MenuAnchorType.PrimaryNotEditable),

                        )

                        ExposedDropdownMenu(
                            expanded = bloodGroupExpanded,
                            onDismissRequest = { bloodGroupExpanded = false }
                        ) {
                            bloodGroups.forEach { bg ->
                                DropdownMenuItem(
                                    text = { Text(bg) },
                                    onClick = {
                                        viewModel.onBloodGroupChange(bg)
                                        bloodGroupExpanded = false
                                    }
                                )
                            }
                        }
                    }

            }

            // Create Ticket Button
            Button(
                onClick = { viewModel.submitTicket(userId = "1") },
                enabled = !uiState.isSubmitting,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(Icons.Default.CheckCircle, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text(if (uiState.isSubmitting) "Creating..." else "Create Ticket")
            }

            // Error / Success Messages
            uiState.errorMessage?.let {
                Text(
                    it,
                    color = Color.Red,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

@Composable
fun CurvedTopBar(
    title: String = "🩺 Admin Panel"
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
        Text(
            text = title,
            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
            color = onPrimary,
            modifier = Modifier
                .align(Alignment.Center)
        )
    }
}



