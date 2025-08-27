package com.example.mediline.User.ui.viewTicket

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mediline.User.data.model.TicketStatus
import com.example.mediline.User.dl.GetTicketsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ViewTicketViewModel @Inject constructor(
    private val getTicketsUseCase: GetTicketsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<TicketUiState>(TicketUiState.Loading)
    val uiState: StateFlow<TicketUiState> = _uiState.asStateFlow()

    fun loadTickets() {
        viewModelScope.launch {
            getTicketsUseCase().fold(
                onSuccess = { tickets ->
                    // Change .find to .filter to get a List<Form>
                    val activeTickets = tickets.filter { it.ticketStatus == TicketStatus.ACTIVE  }
                    val history = tickets.filter { it.ticketStatus != TicketStatus.ACTIVE && it.ticketStatus!= TicketStatus.NULL }
                        .sortedByDescending { it.timeStamp }

                    // Pass the list to the UI state
                    _uiState.value = TicketUiState.Success(activeTickets, history)
                },
                onFailure = {
                    _uiState.value = TicketUiState.Error(it.localizedMessage ?: "Unknown error")
                }
            )
        }
    }
}

//@HiltViewModel
//class ViewTicketViewModel @Inject constructor(
//    private val getTicketsUseCase: GetTicketsUseCase
//) : ViewModel() {
//
//    private val _uiState = MutableStateFlow<TicketUiState>(TicketUiState.Loading)
//    val uiState: StateFlow<TicketUiState> = _uiState.asStateFlow()
//
//    fun loadTickets() {
//        viewModelScope.launch {
//            getTicketsUseCase().fold(
//                onSuccess = { tickets ->
//                    val active = tickets.find { it.ticketStatus == TicketStatus.ACTIVE }
//                    val history = tickets.filter { it.ticketStatus != TicketStatus.ACTIVE }
//                        .sortedByDescending { it.timeStamp }
//                    _uiState.value = TicketUiState.Success(active, history)
//                },
//                onFailure = {
//                    _uiState.value = TicketUiState.Error(it.localizedMessage ?: "Unknown error")
//                }
//            )
//        }
//    }
//}


