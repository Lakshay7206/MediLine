// AdminDepartmentViewModel.kt
package com.example.mediline.ui.admin.department // Or your chosen package

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mediline.data.room.DepartmentEntity
import com.example.mediline.dl.CreateDepartmentUseCase
import com.example.mediline.dl.DeleteDepartmentUseCase
import com.example.mediline.dl.GetDepartmentsUseCase
import com.example.mediline.dl.SyncDepartmentsUseCase
import com.example.mediline.dl.UpdateDepartmentUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AdminDepartmentScreenState(
    val departments: List<DepartmentEntity> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val successMessage: String? = null,
    val isFormVisible: Boolean = false, // To show/hide create/update form

    // Form fields - These are for the input fields
    val currentEditDepartmentId: String? = null, // To know if we are editing or creating
    val departmentNameInput: String = "",
    val departmentDescriptionInput: String = "",
    val departmentDoctorInput: String = "",
    val departmentFeesInput: String = "" // String for TextField, convert to Int on save
)

@HiltViewModel
class AdminDepartmentViewModel @Inject constructor(
    private val getDepartmentsUseCase: GetDepartmentsUseCase, // Replace with your use casertmentsUseCase,
    private val createDepartmentUseCase: CreateDepartmentUseCase,
    private val updateDepartmentUseCase: UpdateDepartmentUseCase,
    private val deleteDepartmentUseCase: DeleteDepartmentUseCase,
    private val syncDepartmentsUseCase: SyncDepartmentsUseCase
    // getDepartmentByIdUseCase is not directly used here but could be if needed
) : ViewModel() {

    private val _uiState = MutableStateFlow(AdminDepartmentScreenState())
    val uiState: StateFlow<AdminDepartmentScreenState> = _uiState.asStateFlow()

    init {
        loadDepartmentsFromRoom()
        // Optionally, trigger a sync on init or provide a manual sync button
        triggerSyncWithFirestore()
    }

    private fun loadDepartmentsFromRoom() {
        val errorHandler = CoroutineExceptionHandler { _, exception ->
            _uiState.update {
                it.copy(
                    isLoading = false,
                    // Use a more generic error message or log the specific exception for debugging
                    error = "An unexpected error occurred: ${exception.localizedMessage}"
                )
            }
            // You might also want to log the exception here using Timber or Log.e
            Log.e("DepartmentViewModel", "Unhandled exception in loadDepartmentsFromRoom", exception)
        }

        viewModelScope.launch(errorHandler) { // Add the handler here
            _uiState.update { it.copy(isLoading = true, error = null) } // Clear previous error
            getDepartmentsUseCase()
                .catch { e ->
                    // This specific catch for the Flow is still good for Flow-specific errors
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = "Failed to load departments: ${e.localizedMessage}"
                        )
                    }
                }
                .collect { departmentsList ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            departments = departmentsList,
                            error = null // Explicitly clear error on success
                        )
                    }
                }
        }
    }

    fun triggerSyncWithFirestore() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, successMessage = null, error = null) }
            val result = syncDepartmentsUseCase()
            result.fold(
                onSuccess = {
                    _uiState.update { it.copy(isLoading = false, successMessage = "Departments synced successfully!") }
                    // The flow from loadDepartmentsFromRoom will automatically update the list
                },
                onFailure = { e ->
                    _uiState.update { it.copy(isLoading = false, error = "Sync failed: ${e.localizedMessage}") }
                }
            )
        }
    }

    fun onDepartmentNameChanged(name: String) {
        _uiState.update { it.copy(departmentNameInput = name, error = null, successMessage = null) }
    }

    fun onDepartmentDescriptionChanged(description: String) {
        _uiState.update { it.copy(departmentDescriptionInput = description, error = null, successMessage = null) }
    }

    fun onDepartmentDoctorChanged(doctor: String) {
        _uiState.update { it.copy(departmentDoctorInput = doctor, error = null, successMessage = null) }
    }

    fun onDepartmentFeesChanged(fees: String) {
        _uiState.update { it.copy(departmentFeesInput = fees, error = null, successMessage = null) }
    }

    fun showCreateDepartmentForm() {
        _uiState.update {
            it.copy(
                isFormVisible = true,
                currentEditDepartmentId = null, // Clear any previous edit state
                departmentNameInput = "",
                departmentDescriptionInput = "",
                departmentDoctorInput = "",
                departmentFeesInput = "",
                error = null,
                successMessage = null
            )
        }
    }

    fun loadDepartmentForEdit(department: DepartmentEntity) {
        _uiState.update {
            it.copy(
                isFormVisible = true,
                currentEditDepartmentId = department.id,
                departmentNameInput = department.name,
                departmentDescriptionInput = department.description,
                departmentDoctorInput = department.doctor,
                departmentFeesInput = department.fees.toString(),
                error = null,
                successMessage = null
            )
        }
    }

    fun dismissForm() {
        _uiState.update { it.copy(isFormVisible = false, currentEditDepartmentId = null, error = null, successMessage = null) }
    }

    fun saveDepartment() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null, successMessage = null) }

            val name = _uiState.value.departmentNameInput.trim()
            val description = _uiState.value.departmentDescriptionInput.trim()
            val doctor = _uiState.value.departmentDoctorInput.trim()
            val feesInt = _uiState.value.departmentFeesInput.trim().toIntOrNull()

            if (name.isBlank() || description.isBlank() || doctor.isBlank() || feesInt == null) {
                _uiState.update { it.copy(isLoading = false, error = "All fields are required and fees must be a valid number.") }
                return@launch
            }
            if (feesInt < 0) {
                _uiState.update { it.copy(isLoading = false, error = "Fees cannot be negative.") }
                return@launch
            }


            val department = DepartmentEntity(
                id = _uiState.value.currentEditDepartmentId ?: "", // If blank, repo handles ID generation
                name = name,
                description = description,
                doctor = doctor,
                fees = feesInt
            )

            val result = runCatching {
                if (_uiState.value.currentEditDepartmentId == null) {
                    createDepartmentUseCase(department) // just Unit
                } else {
                    updateDepartmentUseCase(department) // just Unit
                }
            }

            result.fold(
                onSuccess = {
                    val successMsg = if (_uiState.value.currentEditDepartmentId == null) "Department created!" else "Department updated!"
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            successMessage = successMsg,
                            isFormVisible = false,
                            currentEditDepartmentId = null,
                            departmentNameInput = "",
                            departmentDescriptionInput = "",
                            departmentDoctorInput = "",
                            departmentFeesInput = ""
                        )
                    }
                },
                onFailure = { e ->
                    _uiState.update {
                        it.copy(isLoading = false, error = "Operation failed: ${e.localizedMessage}")
                    }
                }
            )

        }
    }

    fun deleteDepartment(departmentId: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null, successMessage = null) }
            val result = deleteDepartmentUseCase(departmentId)
            result.fold(
                onSuccess = {
                    _uiState.update { it.copy(isLoading = false, successMessage = "Department deleted successfully!") }
                    // List will update via the Flow from Room
                },
                onFailure = { e ->
                    _uiState.update { it.copy(isLoading = false, error = "Delete failed: ${e.localizedMessage}") }
                }
            )
        }
    }
}
