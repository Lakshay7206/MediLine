package com.example.mediline.User.ui.Queue


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mediline.dl.GetQueueLengthUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QueueViewModel @Inject constructor(
    private val getQueueLengthUseCase: GetQueueLengthUseCase
) : ViewModel() {

    private val _queueState = MutableStateFlow<QueueUiState>(QueueUiState.Loading)
    val queueState: StateFlow<QueueUiState> = _queueState

    private var queueJob: Job? = null

    fun observeQueue(deptId: String) {
        queueJob?.cancel() // cancel old job if dept changes
        queueJob = viewModelScope.launch {
            val result = getQueueLengthUseCase(deptId)
            result.fold(
                onSuccess = { queueNumber ->
                    _queueState.value = QueueUiState.Success(queueNumber)
                },
                onFailure = { error ->
                    _queueState.value = QueueUiState.Error(error.message ?: "Unknown error")
                }
            )
        }
    }
}
