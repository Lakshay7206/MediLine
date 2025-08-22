package com.example.mediline.User.ui.Home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mediline.User.data.model.Department
import com.example.mediline.User.data.model.DepartmentRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repo: DepartmentRepository
) : ViewModel() {

    val departments: StateFlow<List<Department>> = repo.getDepartments()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
}
