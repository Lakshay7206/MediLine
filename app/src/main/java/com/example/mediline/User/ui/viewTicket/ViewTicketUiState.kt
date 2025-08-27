package com.example.mediline.User.ui.viewTicket

import com.example.mediline.User.data.model.Form


sealed class TicketUiState {
    object Loading : TicketUiState()
    data class Success(
        val activeTickets: List<Form>,  // Change this to a List
        val history: List<Form>
    ) : TicketUiState()
    data class Error(val message: String) : TicketUiState()
}