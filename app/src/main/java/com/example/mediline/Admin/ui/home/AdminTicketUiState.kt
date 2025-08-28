package com.example.mediline.Admin.ui.home

import com.example.mediline.data.model.Form
import com.example.mediline.data.model.PaymentStatus
import com.example.mediline.data.model.TicketStatus

// UI state for Admin Ticket Screen
data class AdminTicketUiState(
    var tickets: List<Form> = emptyList(),
    var loading: Boolean = false,
    val filters: TicketFilters = TicketFilters(), // currently applied filters
    val selectedFilter: TicketFilters = TicketFilters() // for when admin selects new filters before applying
)

// Represents all available filters
data class TicketFilters(
    val ticketNumber: Int? = null,
    val department: String? = null,
    val status: TicketStatus? = null,        // Active, Closed, Skipped, etc.
    val paymentStatus: PaymentStatus? = null,// Paid, Unpaid, Failed
    val departmentId: Int? = null,        // e.g. "cardiology"
    val searchQuery: String = ""             // search by name, opdNo, etc.
)
