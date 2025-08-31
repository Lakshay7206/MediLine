package com.example.mediline.superAdmin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mediline.dl.AcceptInviteUseCase
import com.example.mediline.dl.InviteAdminUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AdminViewModel @Inject constructor(
    private val acceptInviteUseCase: AcceptInviteUseCase,
    private val sendInviteUseCase: InviteAdminUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<AdminUiState>(AdminUiState.Idle)
    val uiState: StateFlow<AdminUiState> = _uiState

    fun sendInvite(email: String) {
        viewModelScope.launch {
            _uiState.value = AdminUiState.Loading
            val result = sendInviteUseCase(email)
            _uiState.value = if (result.isSuccess) AdminUiState.Success("Invite sent")
            else AdminUiState.Error(result.exceptionOrNull()?.message ?: "Error")
        }
    }

    fun acceptInvite(token: String, password: String) {
        viewModelScope.launch {
            _uiState.value = AdminUiState.Loading
            val result = acceptInviteUseCase(token, password)
            _uiState.value = if (result.isSuccess) AdminUiState.Success("Admin created")
            else AdminUiState.Error(result.exceptionOrNull()?.message ?: "Error")
        }
    }
}

sealed class AdminUiState {
    object Idle : AdminUiState()
    object Loading : AdminUiState()
    data class Success(val message: String) : AdminUiState()
    data class Error(val message: String) : AdminUiState()
}
