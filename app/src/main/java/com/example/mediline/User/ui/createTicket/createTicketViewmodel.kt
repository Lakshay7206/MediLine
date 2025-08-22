package com.example.mediline.User.ui.createTicket

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mediline.User.dl.AddFormUseCase
import com.example.mediline.User.data.model.Form
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class CreateTicketViewModel @Inject constructor(
    private val createTicketUseCase: AddFormUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(CreateTicketUiState())
    val uiState: StateFlow<CreateTicketUiState> = _uiState
    fun addFormFromState(){
        val currentState = uiState.value
        val form = Form(
            name = currentState.name,
            address = currentState.address,
            phone = currentState.phone,
            age = currentState.age,
            sex = currentState.sex,
            departmentId = 123,
            userId = "",
            opdNo = "123",
            timeStamp = System.currentTimeMillis()
        )
        addForm(form)
    }

    fun addForm(form: Form) {
        viewModelScope.launch {
            val result = createTicketUseCase(form)
            result.onSuccess { id ->
                Log.d("AddForm", "Form added with ID: $id")
                // handle success (maybe update a success state)
            }.onFailure { error ->
                Log.d("AddForm", "Error adding form: $error")
                // handle error (update error state)
            }
        }
    }

    fun updateName(name: String) {
        _uiState.value = _uiState.value.copy(name = name)
    }

    fun updateAddress(address: String) {
        _uiState.value = _uiState.value.copy(address = address)
    }

    fun updatePhone(phone: String) {
        _uiState.value = _uiState.value.copy(phone = phone)
    }

    fun updateAge(age: String) { // store as String
        _uiState.value = _uiState.value.copy(age = age.toIntOrNull() ?: 0)
    }

    fun updateSex(sex: Sex) {
        _uiState.value = _uiState.value.copy(sex = sex)
    }
}

