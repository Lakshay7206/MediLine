package com.example.mediline.User.ui.Queue

sealed class QueueUiState {
    object Loading : QueueUiState()
    data class Success(val queueNumber: Int) : QueueUiState()
    data class Error(val message: String) : QueueUiState()
}
