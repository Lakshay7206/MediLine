package com.example.mediline.Admin.ui.home

import com.example.mediline.data.model.Form
import com.example.mediline.data.model.PaymentStatus
import com.example.mediline.data.model.TicketStatus

// UI state for Admin Ticket Screen
data class AdminTicketUiState(
    val tickets: List<Form> = emptyList(),
    val filteredTickets: List<Form> = tickets, // filtered tickets
    val isLoading: Boolean = false,
    val error: String? = null,
    val filter: TicketFilters = TicketFilters()
)

// Represents all available filters
data class TicketFilters(
    val todayCounter: Int? = null,
    val ticketStatus: TicketStatus? = null,        // Active, Closed, Skipped, etc.
    val paymentStatus: PaymentStatus? = null,// Paid, Unpaid, Failed
    val departmentId: String = "",
    val searchQuery: String = ""           // search by name, opdNo, etc.
)
