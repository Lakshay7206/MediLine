package com.example.mediline.superAdmin

import com.example.mediline.data.model.AdminProfile

data class SuperAdminUiState(
    val admins: List<AdminProfile> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

sealed class AdminUiState {
    object Idle : AdminUiState()
    object Loading : AdminUiState()
    data class Success(val message: String) : AdminUiState()
    data class Error(val message: String) : AdminUiState()
}
