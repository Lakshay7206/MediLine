
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.mediline.data.model.Form
import com.example.mediline.User.ui.viewTicket.TicketUiState
import com.example.mediline.User.ui.viewTicket.ViewTicketViewModel


// -------------------- Screen --------------------

@Composable
fun TicketScreen(

    viewModel: ViewTicketViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadTickets()
    }

    when (state) {
        is TicketUiState.Loading -> {
            CircularProgressIndicator()
        }

        is TicketUiState.Error -> {
            Text("Error: ${(state as TicketUiState.Error).message}")
        }

        is TicketUiState.Success -> {
            val successState = state as TicketUiState.Success

            LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp)) {
                // Use `items` to iterate through the list of active tickets
                items(successState.activeTickets) { form ->
                    TicketCard(ticket = form, isActive = true)
                    Spacer(Modifier.height(16.dp))
                }

                // Conditionally show the "History" header only if there are active tickets
                if (successState.activeTickets.isNotEmpty()) {
                    item {
                        Text("History", style = MaterialTheme.typography.titleMedium)
                        Spacer(Modifier.height(8.dp))
                    }
                }

                // Display the history tickets
                items(successState.history) { form ->
                    TicketCard(ticket = form, isActive = false)
                    Spacer(Modifier.height(8.dp))
                }
            }
        }
    }
}

@Composable
fun TicketCard(ticket: Form, isActive: Boolean) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(6.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isActive) MaterialTheme.colorScheme.primaryContainer
            else MaterialTheme.colorScheme.surface
        )
    ) {
        Column(Modifier.padding(16.dp)) {
            Text("Ticket #${ticket.ticketNumber}", style = MaterialTheme.typography.titleMedium)
            Text("Name: ${ticket.name}")
            Text("Age: ${ticket.age}, Sex: ${ticket.sex}")
            Text("Phone: ${ticket.phone}")
            Text("Address: ${ticket.address}")
            Text("Status: ${ticket.ticketStatus}")
            Text("Date: ${java.text.SimpleDateFormat("dd MMM yyyy, hh:mm a").format(ticket.timeStamp)}")
            if (isActive) {
                Text("Status: Active", color = Color.Green, style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}
