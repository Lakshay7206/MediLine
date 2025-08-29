package com.example.mediline.User.ui.Home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mediline.data.model.Department
import com.example.mediline.data.room.DepartmentEntity
import com.example.mediline.dl.CreateDepartmentUseCase
import com.example.mediline.dl.GetDepartmentsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DepartmentViewModel @Inject constructor(
    private val getDepartmentsUseCase: GetDepartmentsUseCase
) : ViewModel() {

    private val _departments = MutableStateFlow<List<DepartmentEntity>>(emptyList())
    val departments: StateFlow<List<DepartmentEntity>> = _departments

    init {
        viewModelScope.launch {
            try {
                getDepartmentsUseCase()
                    .catch { e ->
                        Log.e("DepartmentViewModel", "Error fetching departments", e)
                        emit(emptyList())
                    }
                    .collect { list ->
                        _departments.value = list
                    }
            } catch (e: Exception) {
                Log.e("DepartmentViewModel", "Unexpected error", e)
            }
        }
    }

//    val departments: StateFlow<List<Department>> =
//        getDepartmentsUseCase()
//            .catch { e ->
//                Log.e("DepartmentViewModel", "Error fetching departments", e)
//                emit(emptyList()) // fallback to empty list if error
//            }
//            .stateIn(
//                scope = viewModelScope,
//                started = SharingStarted.Lazily,
//                initialValue = emptyList()
//            )
}
