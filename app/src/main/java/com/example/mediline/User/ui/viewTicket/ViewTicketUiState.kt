package com.example.mediline.User.ui.viewTicket

import com.example.mediline.data.model.Form
import java.io.File


sealed class TicketUiState {
    object Loading : TicketUiState()
    data class Success(
        val activeTickets: List<Form>,  // Change this to a List
        val history: List<Form>,
        val ticketFile: File? = null
    ) : TicketUiState()
    data class Error(val message: String) : TicketUiState()
}