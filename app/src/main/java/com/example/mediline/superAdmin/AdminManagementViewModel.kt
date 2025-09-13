package com.example.mediline.superAdmin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mediline.dl.AcceptInviteUseCase
import com.example.mediline.dl.DeleteAdminUseCase
import com.example.mediline.dl.GetAllAdminsUseCase
import com.example.mediline.dl.InviteAdminUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import androidx.lifecycle.viewModelScope
import com.example.mediline.data.model.AdminProfile
import com.example.mediline.data.repo.AdminRepository

import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch




@HiltViewModel
class SuperAdminViewModel @Inject constructor(
    private val getAllAdminsUseCase: GetAllAdminsUseCase,
    private val deleteAdminUseCase: DeleteAdminUseCase,
    private val acceptInviteUseCase: AcceptInviteUseCase,
    private val sendInviteUseCase: InviteAdminUseCase
) : ViewModel() {

    // -----------------------------
    // Super Admin State
    // -----------------------------
    private val _superAdminUiState = MutableStateFlow(SuperAdminUiState())
    val superAdminUiState: StateFlow<SuperAdminUiState> = _superAdminUiState.asStateFlow()

    init {
        loadAllAdmins()
    }

    /** Loads all admins from repository */
    fun loadAllAdmins() {
        viewModelScope.launch {
            _superAdminUiState.update { it.copy(isLoading = true, error = null) }

            try {
                val admins = getAllAdminsUseCase()
                _superAdminUiState.update {
                    it.copy(
                        isLoading = false,
                        admins = admins,
                        error = null
                    )
                }
            } catch (e: Exception) {
                _superAdminUiState.update {
                    it.copy(
                        isLoading = false,
                        error = e.message ?: "Failed to load admins"
                    )
                }
            }
        }
    }

    /** Removes an admin */
    /** Removes an admin */
    fun removeAdmin(admin: AdminProfile) {
        viewModelScope.launch {
            _superAdminUiState.update { it.copy(isLoading = true, error = null) }

            try {
                // Call the use case to delete the admin by ID
                deleteAdminUseCase(admin.id)

                // Update the UI state by removing the deleted admin
                _superAdminUiState.update { state ->
                    state.copy(
                        isLoading = false,
                        admins = state.admins.filterNot { it.id == admin.id }
                    )
                }
            } catch (e: Exception) {
                _superAdminUiState.update { state ->
                    state.copy(isLoading = false, error = e.message ?: "Failed to remove admin")
                }
            }
        }
    }


    fun retryLoadAdmins() {
        loadAllAdmins()
    }

    // -----------------------------
    // Admin Invite State
    // -----------------------------
    private val _adminUiState = MutableStateFlow<AdminUiState>(AdminUiState.Idle)
    val adminUiState: StateFlow<AdminUiState> = _adminUiState

    /** Send invite to new admin */
    fun sendInvite(email: String) {
        viewModelScope.launch {
            _adminUiState.value = AdminUiState.Loading
            val result = sendInviteUseCase(email)
            _adminUiState.value =
                if (result.isSuccess) AdminUiState.Success("Invite sent")
                else AdminUiState.Error(result.exceptionOrNull()?.message ?: "Error")
        }
    }

    /** Accept invite */
    fun acceptInvite(token: String, password: String) {
        viewModelScope.launch {
            _adminUiState.value = AdminUiState.Loading
            val result = acceptInviteUseCase(token, password)
            _adminUiState.value =
                if (result.isSuccess) AdminUiState.Success("Admin created")
                else AdminUiState.Error(result.exceptionOrNull()?.message ?: "Error")
        }
    }
}
