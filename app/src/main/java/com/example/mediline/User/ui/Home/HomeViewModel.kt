package com.example.mediline.User.ui.Home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mediline.User.data.model.Department
import com.example.mediline.User.data.model.DepartmentRepository
import com.example.mediline.User.dl.CreateDepartmentUseCase
import com.example.mediline.User.dl.VerifyPaymentUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class DepartmentViewModel @Inject constructor(
    private val getDepartmentsUseCase: CreateDepartmentUseCase
) : ViewModel() {

    val departments: StateFlow<List<Department>> =
        getDepartmentsUseCase()
            .catch { e ->
                Log.e("DepartmentViewModel", "Error fetching departments", e)
                emit(emptyList()) // fallback to empty list if error
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.Lazily,
                initialValue = emptyList()
            )
}
