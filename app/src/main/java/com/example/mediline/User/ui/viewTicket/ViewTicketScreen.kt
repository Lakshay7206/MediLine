
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.mediline.data.model.Form
import com.example.mediline.User.ui.viewTicket.TicketUiState
import com.example.mediline.User.ui.viewTicket.ViewTicketViewModel
import com.example.mediline.data.model.TicketStatus
import java.text.SimpleDateFormat
import java.util.Locale

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TicketScreen(
    navigateHome:()-> Unit,
    viewModel: ViewTicketViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) { viewModel.loadTickets() }

    when (state) {
        is TicketUiState.Loading -> {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
            }
        }

        is TicketUiState.Error -> {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(
                    "⚠️ Error: ${(state as TicketUiState.Error).message}",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }

        is TicketUiState.Success -> {
            Scaffold(
                topBar = {
                    CurvedHomeTopBar("View Tickets", true, navigateHome)
                }
            ) { padding ->
                val successState = state as TicketUiState.Success
                val context = LocalContext.current

                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Active Tickets
                    if (successState.activeTickets.isNotEmpty()) {
                        stickyHeader { SectionTitle("Active Tickets") }

                        items(successState.activeTickets) { form ->
                            ViewTicketCard(
                                ticket = form,
                                isActive = true,
                                generatePdf = { viewModel.createAndDownloadTicketFile(context, form) }
                            )
                        }
                    }

                    // History Tickets
                    if (successState.history.isNotEmpty()) {
                        stickyHeader { SectionTitle("History") }

                        items(successState.history) { form ->
                            ViewTicketCard(
                                ticket = form,
                                isActive = false,
                                generatePdf = { viewModel.createAndDownloadTicketFile(context, form) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SectionTitle(title: String) {
    Surface(
        color = MaterialTheme.colorScheme.background,
        shadowElevation = 0.dp
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 6.dp)
        )
    }
}

@Composable
fun CurvedHomeTopBar(
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
                    imageVector = Icons.Default.Home,
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
fun ViewTicketCard(ticket: Form, isActive: Boolean, generatePdf: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp) // subtle shadow
    ) {
        Row(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.surface)
        ) {
            // ✅ Slim green strip for active
            if (isActive) {
                Box(
                    modifier = Modifier
                        .width(6.dp)
                        .fillMaxHeight()
                        .background(Color(0xFF2E7D32), RoundedCornerShape(topStart = 12.dp, bottomStart = 12.dp))
                )
            }

            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Header Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "Ticket #${ticket.ticketNumber}",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    StatusChip(isActive = isActive, status = ticket.ticketStatus)
                }

                // Patient Info
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    InfoRow("👤", "${ticket.name}, ${ticket.age} yrs (${ticket.sex})")
//                    InfoRow("📞", ticket.phone)
//                    InfoRow("🏠", ticket.address)
                    InfoRow(
                        "🕒",
                        SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault()).format(ticket.timeStamp),
                        isSecondary = true
                    )
                }

                // Action Button
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    if (isActive) {
                        Button(
                            onClick = generatePdf,
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF2E7D32),
                                contentColor = Color.White
                            )
                        ) {
                            Icon(Icons.Default.Download, contentDescription = "Download")
                            Spacer(Modifier.width(6.dp))
                            Text("Download")
                        }
                    } else {
                        OutlinedButton(
                            onClick = generatePdf,
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = MaterialTheme.colorScheme.primary
                            )
                        ) {
                            Icon(Icons.Default.Download, contentDescription = "Download")
                            Spacer(Modifier.width(6.dp))
                            Text("Download")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun InfoRow(icon: String, text: String, isSecondary: Boolean = false) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(icon, modifier = Modifier.width(22.dp))
        Spacer(Modifier.width(4.dp))
        Text(
            text,
            style = if (isSecondary) MaterialTheme.typography.bodySmall else MaterialTheme.typography.bodyMedium,
            color = if (isSecondary) MaterialTheme.colorScheme.onSurfaceVariant
            else MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
fun StatusChip(isActive: Boolean, status: TicketStatus) {
    Box(
        modifier = Modifier
            .background(
                color = if (isActive) Color(0xFF4CAF50).copy(alpha = 0.15f) else Color(0xFF9E9E9E).copy(alpha = 0.15f),
                shape = RoundedCornerShape(50)
            )
            .padding(horizontal = 12.dp, vertical = 4.dp)
    ) {
        Text(
            text = if (isActive) "Active" else status.name.lowercase().replaceFirstChar { it.uppercase() },
            color = if (isActive) Color(0xFF2E7D32) else Color(0xFF616161),
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.Medium
        )
    }
}



