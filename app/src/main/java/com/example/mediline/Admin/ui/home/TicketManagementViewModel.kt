package com.example.mediline.Admin.ui.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mediline.User.ui.viewTicket.TicketUiState
import com.example.mediline.data.model.Form
import com.example.mediline.data.model.FormEntity
import com.example.mediline.data.model.PaymentStatus
import com.example.mediline.data.model.TicketStatus
import com.example.mediline.data.repo.AdminTicketRepository
import com.example.mediline.dl.GetAllTicketsUseCase
import com.example.mediline.dl.UpdatePaymentStatusUseCase
import com.example.mediline.dl.UpdateTicketStatusUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
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

//
//
//        // Filtered tickets as a derived Flow
//        val filteredTickets: StateFlow<List<Form>> = uiState
//            .map { state ->
//                state.tickets.filter { ticket ->
//                    (state.filter.ticketStatus?.let { ticket.ticketStatus == it } ?: true) &&
//                            (state.filter.departmentId?.let { ticket.departmentId == it.toString() } ?: true) &&
//                            (state.filter.todayCounter?.let { ticket.ticketNumber == it.toLong() } ?: true) &&
//                            (state.filter.paymentStatus?.let { ticket.paymentStatus == it } ?: true) &&
//                            (state.filter.searchQuery.takeIf { it.isNotBlank() }?.let {
//                                ticket.name.contains(it, ignoreCase = true) ||
//                                        ticket.opdNo.contains(it, ignoreCase = true)
//                            } ?: true)
//                }
//            }
//            .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
//
//        // Update filters (reactive)
//        fun setFilter(filter: TicketFilters) {
//            _uiState.update { it.copy(filter = filter) }
//        }
//
//        // Debounced search
//        private var searchJob: Job? = null
//        fun onSearchQueryChanged(query: String) {
//            searchJob?.cancel()
//            searchJob = viewModelScope.launch {
//                delay(300) // debounce
//                setFilter(uiState.value.filter.copy(searchQuery = query))
//            }
//        }



    fun loadTickets() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val result = getAllTicketsUseCase()
            _uiState.update {
                it.copy(
                    tickets = result.getOrElse { emptyList() },
                    isLoading = false,
                    filteredTickets = result.getOrElse { emptyList() }
                )
            }
        }
    }

    fun updateTicketStatus(ticketId: String, status: TicketStatus) {
        viewModelScope.launch {
            Log.d("AdminTicketViewModel", "Updating status for ticket: $ticketId to $status")
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
    fun applyFilters(tickets: List<Form>, filter: TicketFilters): List<Form> {
        return tickets.filter { ticket ->
            filter.ticketStatus?.let { ticket.ticketStatus == it } ?: true &&
                    filter.departmentId?.let { it == "0" ||ticket.departmentId== it } ?: true &&
                    filter.todayCounter?.let { ticket.ticketNumber.toInt() == it } ?: true &&
                    filter.paymentStatus?.let { ticket.paymentStatus == it } ?: true &&
                    filter.searchQuery.takeIf { it.isNotBlank() }?.let { query ->
                        ticket.name.contains(query, ignoreCase = true) ||
                                ticket.opdNo.contains(query, ignoreCase = true)
                    } ?: true
        }
    }
    fun setFilter(filter: TicketFilters) {
        val filtered = applyFilters(_uiState.value.tickets, filter)
        _uiState.update { it.copy(filteredTickets = filtered, filter = filter) }
    }


    fun skipTicket(ticketId: String) {
        Log.d("AdminTicketViewModel", "Skipping ticket: $ticketId")
        updateTicketStatus(ticketId, TicketStatus.SKIPPED)
    }

    fun cancelTicket(ticketId: String) {
        Log.d("AdminTicketViewModel", "Cancelling ticket: $ticketId")
        updateTicketStatus(ticketId, TicketStatus.CANCELLED)
    }

    fun completeTicket(ticketId: String) {
        Log.d("AdminTicketViewModel", "Completing ticket: $ticketId")
        updateTicketStatus(ticketId, TicketStatus.CLOSED)
    }

    fun reassignTicket(ticketId: String) {
        Log.d("AdminTicketViewModel", "Reassigning ticket: $ticketId")
        updateTicketStatus(ticketId, TicketStatus.ACTIVE)
    }


    init {
        loadTickets()
    }



}
