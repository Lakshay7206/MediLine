package com.example.mediline.Admin.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mediline.User.ui.viewTicket.TicketUiState
import com.example.mediline.data.model.Form
import com.example.mediline.data.model.PaymentStatus
import com.example.mediline.data.model.TicketStatus
import com.example.mediline.data.repo.AdminTicketRepository
import com.example.mediline.dl.GetAllTicketsUseCase
import com.example.mediline.dl.UpdatePaymentStatusUseCase
import com.example.mediline.dl.UpdateTicketStatusUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class AdminTicketViewModel @Inject constructor(
    private val getAllTicketsUseCase: GetAllTicketsUseCase,
    private val updateTicketStatusUseCase: UpdateTicketStatusUseCase,
    private val updatePaymentStatusUseCase: UpdatePaymentStatusUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(AdminTicketUiState())
    val uiState: StateFlow<AdminTicketUiState> = _uiState.asStateFlow()

    fun loadTickets() {
        viewModelScope.launch {
            _uiState.update { it.copy(loading = true) }
            val result = getAllTicketsUseCase()
            _uiState.update {
                it.copy(
                    tickets = result.getOrElse { emptyList() },
                    loading = false
                )
            }
        }
    }

    fun updateTicketStatus(ticketId: String, status: TicketStatus) {
        viewModelScope.launch {
            updateTicketStatusUseCase(ticketId, status)
            loadTickets() // refresh after update
        }
    }

    fun updatePaymentStatus(ticketId: String, status: PaymentStatus) {
        viewModelScope.launch {
            updatePaymentStatusUseCase(ticketId, status)
            loadTickets()
        }
    }

    fun updateFilters(filters: TicketFilters) {
        _uiState.update { it.copy(filters = filters) }
    }

    fun skipTicket(ticketId: String) {
        updateTicketStatus(ticketId, TicketStatus.SKIPPED)
    }

    fun cancelTicket(ticketId: String) {
        updateTicketStatus(ticketId, TicketStatus.CANCELLED)
    }

    fun completeTicket(ticketId: String) {
        updateTicketStatus(ticketId, TicketStatus.CLOSED)
    }

    fun reassignTicket(ticketId: String) {
        updateTicketStatus(ticketId, TicketStatus.ACTIVE)
    }

    init {
        loadTickets()
    }

}
