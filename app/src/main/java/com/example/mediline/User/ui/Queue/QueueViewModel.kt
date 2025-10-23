package com.example.mediline.User.ui.Queue



import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mediline.data.model.Form
import com.example.mediline.data.model.TicketStatus
import com.example.mediline.dl.GetTodaysTicketsUseCase

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class QueueUiState {
    object Loading : QueueUiState()
    data class Success(
        val tickets: List<Form>,
        val currentNumber: Int,
        val totalPatients: Int,
        val ticketBoxes: List<TicketBox>
    ) : QueueUiState()
    data class Error(val message: String) : QueueUiState()
}
data class TicketBox(
    val ticketNumber: Int,
    val status: TicketStatus,
    val color: Color
)
@HiltViewModel
class QueueViewModel @Inject constructor(
    private val getTodaysTicketsUseCase: GetTodaysTicketsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<QueueUiState>(QueueUiState.Loading)
    val uiState: StateFlow<QueueUiState> = _uiState

    fun loadTodaysTickets(departmentId: String) {
        viewModelScope.launch {
           getTodaysTicketsUseCase(departmentId)
                .catch { e ->
                    _uiState.value = QueueUiState.Error(e.message ?: "Unknown error")
                }
                .collect { allTickets ->
                    val activeTickets = allTickets
                       // .filter { it.ticketStatus !in listOf(TicketStatus.CLOSED, TicketStatus.SKIPPED, TicketStatus.CANCELLED) }
                        .sortedBy { it.timeStamp }

                    val currentServing = activeTickets.firstOrNull { it.ticketStatus == TicketStatus.SERVING }?.ticketNumber ?: 0

                    val boxes = activeTickets.map { ticket ->
                        TicketBox(
                            ticketNumber = ticket.ticketNumber.toInt(),
                            status = ticket.ticketStatus,
                            color = when (ticket.ticketStatus) {
                                TicketStatus.SERVING -> Color(0xFF64B5F6)
                                TicketStatus.ACTIVE-> Color(0xFF4CAF50)
                                // TicketStatus.CLOSED ->
                                else -> Color(0xFF9E9E9E)
                            }
                        )
                    }

                    _uiState.value = QueueUiState.Success(
                        tickets = allTickets,
                        totalPatients = activeTickets.size,
                        ticketBoxes = boxes,
                        currentNumber = currentServing.toInt()
                    )
                }
        }


    }

//    fun serveTicket(ticket: Form) {
//        viewModelScope.launch {
//            repository.updateTicketStatus(ticket.id, TicketStatus.SERVING)
//        }
//    }
}

