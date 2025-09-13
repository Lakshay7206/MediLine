package com.example.mediline.Admin.ui.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mediline.User.ui.viewTicket.TicketUiState
import com.example.mediline.data.model.Department
import com.example.mediline.data.model.Form
import com.example.mediline.data.model.FormEntity
import com.example.mediline.data.model.PaymentStatus
import com.example.mediline.data.model.TicketStatus
import com.example.mediline.data.model.toDomain
import com.example.mediline.data.repo.AdminTicketRepository
import com.example.mediline.data.room.DepartmentEntity
import com.example.mediline.dl.GetAllTicketsUseCase
import com.example.mediline.dl.GetDepartmentsUseCase
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
    private val updatePaymentStatusUseCase: UpdatePaymentStatusUseCase,
    private val getDepartmentsUseCase: GetDepartmentsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(AdminTicketUiState())
    val uiState: StateFlow<AdminTicketUiState> = _uiState.asStateFlow()

    init {
        loadTickets()
        getDepartments()
    }

    /** 🔹 Load departments from DB */
    private fun getDepartments() {
        viewModelScope.launch {
            getDepartmentsUseCase().collect { entities ->
                _uiState.update {
                    it.copy(
                        departments = entities.map { entity -> entity.toDomain() }
                    )
                }
            }
        }
    }

    /** 🔹 Load all tickets from DB */
    fun loadTickets() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val result = getAllTicketsUseCase()
            _uiState.update {
                val tickets = result.getOrElse { emptyList() }
                it.copy(
                    tickets = tickets,
                    filteredTickets = applyFilters(tickets, it.filter),
                    isLoading = false
                )
            }
        }
    }

    /** 🔹 Update Ticket Status (optimistic update first, then persist) */
    fun updateTicketStatus(ticketId: String, status: TicketStatus) {
        // ✅ Update state immediately
        _uiState.update { state ->
            val updatedTickets = state.tickets.map { ticket ->
                if (ticket.id == ticketId) ticket.copy(ticketStatus = status) else ticket
            }
            state.copy(
                tickets = updatedTickets,
                filteredTickets = applyFilters(updatedTickets, state.filter)
            )
        }

        // ✅ Persist in background
        viewModelScope.launch {
            try {
                updateTicketStatusUseCase(ticketId, status)
            } catch (e: Exception) {
                Log.e("AdminTicketViewModel", "Failed to update ticket", e)
            }
        }
    }

    /** 🔹 Payment Status Update */
    fun updatePaymentStatus(ticketId: String, status: PaymentStatus) {
        _uiState.update { state ->
            val updatedTickets = state.tickets.map { ticket ->
                if (ticket.id == ticketId) ticket.copy(paymentStatus = status) else ticket
            }
            state.copy(
                tickets = updatedTickets,
                filteredTickets = applyFilters(updatedTickets, state.filter)
            )
        }

        viewModelScope.launch {
            try {
                updatePaymentStatusUseCase(ticketId, status)
            } catch (e: Exception) {
                Log.e("AdminTicketViewModel", "Failed to update payment status", e)
            }
        }
    }

    /** 🔹 Filtering */
    private fun applyFilters(tickets: List<Form>, filter: TicketFilters): List<Form> {
        return tickets.filter { ticket ->
            (filter.ticketStatus?.let { ticket.ticketStatus == it } ?: true) &&
                    (filter.department.isBlank() || ticket.departmentId == filter.department) &&
                    (filter.todayCounter?.let { ticket.ticketNumber == it.toLong() } ?: true) &&
                    (filter.paymentStatus?.let { ticket.paymentStatus == it } ?: true) &&
                    (filter.searchQuery.takeIf { it.isNotBlank() }?.let { query ->
                        ticket.name.contains(query, ignoreCase = true) ||
                                ticket.opdNo.contains(query, ignoreCase = true)
                    } ?: true)
        }
    }

    fun setFilter(filter: TicketFilters) {
        val filtered = applyFilters(_uiState.value.tickets, filter)
        _uiState.update { it.copy(filteredTickets = filtered, filter = filter) }
    }

    /** 🔹 User Actions */
    fun skipTicket(ticketId: String) = updateTicketStatus(ticketId, TicketStatus.SKIPPED)
    fun cancelTicket(ticketId: String) = updateTicketStatus(ticketId, TicketStatus.CANCELLED)
    fun completeTicket(ticketId: String) = updateTicketStatus(ticketId, TicketStatus.CLOSED)
    fun reassignTicket(ticketId: String) {
        viewModelScope.launch {
            val result = updateTicketStatusUseCase(ticketId, TicketStatus.ACTIVE)
            if (result.isSuccess) {
                // Update local state so UI reflects ACTIVE
                _uiState.update { state ->
                    val updatedTickets = state.tickets.map { t ->
                        if (t.id == ticketId) t.copy(ticketStatus = TicketStatus.ACTIVE)
                        else t
                    }
                    val filtered = applyFilters(updatedTickets, state.filter)
                    state.copy(tickets = updatedTickets, filteredTickets = filtered)
                }
            }
        }
    }

}























//@HiltViewModel
//class AdminTicketViewModel @Inject constructor(
//    private val getAllTicketsUseCase: GetAllTicketsUseCase,
//    private val updateTicketStatusUseCase: UpdateTicketStatusUseCase,
//    private val updatePaymentStatusUseCase: UpdatePaymentStatusUseCase,
//    private val getDepartmentsUseCase : GetDepartmentsUseCase
//) : ViewModel() {
//
//    private val _uiState = MutableStateFlow(AdminTicketUiState())
//    val uiState: StateFlow<AdminTicketUiState> = _uiState.asStateFlow()
//
////
////
////        // Filtered tickets as a derived Flow
////        val filteredTickets: StateFlow<List<Form>> = uiState
////            .map { state ->
////                state.tickets.filter { ticket ->
////                    (state.filter.ticketStatus?.let { ticket.ticketStatus == it } ?: true) &&
////                            (state.filter.departmentId?.let { ticket.departmentId == it.toString() } ?: true) &&
////                            (state.filter.todayCounter?.let { ticket.ticketNumber == it.toLong() } ?: true) &&
////                            (state.filter.paymentStatus?.let { ticket.paymentStatus == it } ?: true) &&
////                            (state.filter.searchQuery.takeIf { it.isNotBlank() }?.let {
////                                ticket.name.contains(it, ignoreCase = true) ||
////                                        ticket.opdNo.contains(it, ignoreCase = true)
////                            } ?: true)
////                }
////            }
////            .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
////
////        // Update filters (reactive)
////        fun setFilter(filter: TicketFilters) {
////            _uiState.update { it.copy(filter = filter) }
////        }
////
////        // Debounced search
////        private var searchJob: Job? = null
////        fun onSearchQueryChanged(query: String) {
////            searchJob?.cancel()
////            searchJob = viewModelScope.launch {
////                delay(300) // debounce
////                setFilter(uiState.value.filter.copy(searchQuery = query))
////            }
////        }
//
//    fun getDepartments() {
//        viewModelScope.launch {
//            getDepartmentsUseCase().collect { entities ->
//                _uiState.update {
//                    it.copy(
//                        departments = entities.map { it.toDomain() }
//                    )
//                }
//            }
//        }
//    }
//
//
//
//
//
//
//
//
//    fun loadTickets() {
//        viewModelScope.launch {
//            _uiState.update { it.copy(isLoading = true) }
//            val result = getAllTicketsUseCase()
//            _uiState.update {
//                it.copy(
//                    tickets = result.getOrElse { emptyList() },
//                    isLoading = false,
//                    filteredTickets = result.getOrElse { emptyList() }
//                )
//            }
//        }
//    }
//
//    fun updatePaymentStatus(ticketId: String, status: PaymentStatus) {
//        viewModelScope.launch {
//            updatePaymentStatusUseCase(ticketId, status)
//            loadTickets()
//        }
//    }
//
//    fun updateTicketStatus(ticketId: String, status: TicketStatus) {
//        viewModelScope.launch {
//            Log.d("AdminTicketViewModel", "Updating status for ticket: $ticketId to $status")
//            updateTicketStatusUseCase(ticketId, status)
//            loadTickets() // refresh after update
//        }
//    }
//
//
//    fun applyFilters(tickets: List<Form>, filter: TicketFilters): List<Form> {
//        return tickets.filter { ticket ->
//            (filter.ticketStatus?.let { ticket.ticketStatus == it } ?: true) &&
//                    (filter.department.isBlank() || ticket.departmentId == filter.department) &&
//                    (filter.todayCounter?.let { ticket.ticketNumber == it.toLong() } ?: true) &&
//                    (filter.paymentStatus?.let { ticket.paymentStatus == it } ?: true) &&
//                    (filter.searchQuery.takeIf { it.isNotBlank() }?.let { query ->
//                        ticket.name.contains(query, ignoreCase = true) ||
//                                ticket.opdNo.contains(query, ignoreCase = true)
//                    } ?: true)
//        }
//    }
//
//
//    fun setFilter(filter: TicketFilters) {
//        val filtered = applyFilters(_uiState.value.tickets, filter)
//        _uiState.update { it.copy(filteredTickets = filtered, filter = filter) }
//    }
//
//
//
//    fun skipTicket(ticketId: String) {
//        Log.d("AdminTicketViewModel", "Skipping ticket: $ticketId")
//        updateTicketStatus(ticketId, TicketStatus.SKIPPED)
//    }
//
//    fun cancelTicket(ticketId: String) {
//        Log.d("AdminTicketViewModel", "Cancelling ticket: $ticketId")
//        updateTicketStatus(ticketId, TicketStatus.CANCELLED)
//    }
//
//    fun completeTicket(ticketId: String) {
//        Log.d("AdminTicketViewModel", "Completing ticket: $ticketId")
//        updateTicketStatus(ticketId, TicketStatus.CLOSED)
//    }
//
//    fun reassignTicket(ticketId: String) {
//        Log.d("AdminTicketViewModel", "Reassigning ticket: $ticketId")
//        updateTicketStatus(ticketId, TicketStatus.ACTIVE)
//    }
//
//
//    init {
//        loadTickets()
//        getDepartments()
//    }
//
//
//
//}
