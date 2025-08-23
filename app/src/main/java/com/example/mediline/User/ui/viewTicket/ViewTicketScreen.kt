package com.example.mediline.User.ui.viewTicket

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.mediline.User.data.model.Form
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.mediline.User.data.model.PaymentStatus
import com.example.mediline.User.data.model.TicketStatus
import com.example.mediline.User.ui.createTicket.Sex


@Composable
fun ViewTicketsScreen(deptId: String) {
    val currentTicket: Form? =dummyForm
   val pastTickets: List<Form> =dummyTickets
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {

        Text("My Tickets", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(16.dp))

        currentTicket?.let {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFDFF6DD))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Current Ticket", style = MaterialTheme.typography.titleMedium)
                    Text("Dept: ${it.departmentId}")
                    Text("Ticket #: ${it.ticketNumber}")
                    Text("Status: ${it.ticketStatus}")
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        Text("Past Tickets", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn {
            items(pastTickets) { ticket ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF0F0F0))
                ) {
                    Row(modifier = Modifier.padding(16.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                        Column {
                            Text("Dept: ${ticket.departmentId}")
                            Text("Ticket #: ${ticket.ticketNumber}")
                        }
                        Text("${ticket.ticketStatus}")
                    }
                }
            }
        }
    }
}
val dummyForm = Form(
    departmentId = "cardio",
    userId = "user123",
    opdNo = "OPD001",
    name = "John Doe",
    address = "123 Main Street",
    phone = "9876543210",
    age = 35,
    sex = Sex.Male, // assuming Sex is an enum
    timeStamp = System.currentTimeMillis(),
    ticketNumber = 1,
    paymentStatus = PaymentStatus.Unpaid, // assuming PaymentStatus is an enum
    ticketStatus = TicketStatus.Open // assuming TicketStatus is an enum
)
val dummyTickets = listOf(
    Form(
        departmentId = "cardio",
        userId = "user123",
        opdNo = "OPD001",
        name = "John Doe",
        address = "123 Main Street",
        phone = "9876543210",
        age = 35,
        sex = Sex.Male,
        timeStamp = System.currentTimeMillis() - 86400000L, // 1 day ago
        ticketNumber = 1,
        paymentStatus = PaymentStatus.Paid,
        ticketStatus = TicketStatus.Closed
    ),
    Form(
        departmentId = "neuro",
        userId = "user123",
        opdNo = "OPD002",
        name = "John Doe",
        address = "123 Main Street",
        phone = "9876543210",
        age = 35,
        sex = Sex.Male,
        timeStamp = System.currentTimeMillis() - 3600000L, // 1 hour ago
        ticketNumber = 2,
        paymentStatus = PaymentStatus.Unpaid,
        ticketStatus = TicketStatus.Open
    ),
    Form(
        departmentId = "ortho",
        userId = "user123",
        opdNo = "OPD003",
        name = "John Doe",
        address = "123 Main Street",
        phone = "9876543210",
        age = 35,
        sex = Sex.Male,
        timeStamp = System.currentTimeMillis(),
        ticketNumber = 3,
        paymentStatus = PaymentStatus.Unpaid,
        ticketStatus = TicketStatus.Open
    )
)
