package com.example.mediline.User.ui.createTicket

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mediline.data.model.CreatorRole
import com.example.mediline.dl.AddFormUseCase
import com.example.mediline.data.model.Form
import com.example.mediline.data.model.PaymentStatus
import com.example.mediline.data.model.TicketStatus
import com.example.mediline.data.room.DepartmentEntity
import com.example.mediline.dl.GetDepartmentByIdUseCase

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class CreateTicketViewModel @Inject constructor(
    private val createTicketUseCase: AddFormUseCase,
    private val getDepartmentByIdUseCase: GetDepartmentByIdUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(CreateTicketUiState())
    val uiState: StateFlow<CreateTicketUiState> = _uiState

    private val _department = MutableStateFlow<DepartmentEntity?>(null)
    val department: StateFlow<DepartmentEntity?> = _department

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()


    fun loadDepartment(id: String) {
        viewModelScope.launch {
            getDepartmentByIdUseCase(id).collect { dept ->
                _department.value = dept
            }
        }
    }

//    fun validate(): Boolean {
//        val state = _uiState.value
//        val errors = mutableMapOf<String, String?>()
//
//        if (state.name.trim().isBlank()) {
//            errors["name"] = "Name is required"
//        }
//
//        if (state.phone.isNotBlank() && !state.phone.matches(Regex("^\\d{10}$"))) {
//            errors["phone"] = "Phone must be 10 digits"
//        }
//
//        if (state.age <= 0 || state.age > 120) {
//            errors["age"] = "Enter valid age"
//        }
//
//        if (state.departmentId.isBlank()) {
//            errors["department"] = "Department is required"
//        }
//
//        // optional fields: validate only if non-empty
//
//
//        return errors.isEmpty()
//    }
fun validate(): Boolean {
    val state = _uiState.value
    val errors = mutableMapOf<String, String?>()

    if (state.name.trim().isBlank()) {
        errors["name"] = "Name is required"
    }

    if (state.phone.isNotBlank() && !state.phone.matches(Regex("^\\d{10}$"))) {
        errors["phone"] = "Phone must be 10 digits"
    }

    val ageInt = state.age.toIntOrNull()
    if (ageInt == null || ageInt <= 0 || ageInt > 120) {
        errors["age"] = "Enter valid age"
    }

    if (state.address.trim().isBlank()) {
        errors["address"] = "Address is required"
    }

    _uiState.value = _uiState.value.copy(errors = errors) // push errors to state
    return errors.isEmpty()
}


    fun addFormFromState(){
       if (!validate()) return
        val currentState = uiState.value
        val form = Form(
            name = currentState.name,
            address = currentState.address,
            phone = currentState.phone,
            age = currentState.age.toInt() ,
            sex = currentState.sex,
            userId = "",
            opdNo = "123",
            timeStamp = System.currentTimeMillis(),
            paymentStatus = PaymentStatus.UNPAID,
            ticketStatus = TicketStatus.ACTIVE,
            departmentId =currentState.departmentId,
            createdBy = "",
            creatorRole = CreatorRole.USER

        )
        addForm(form)
    }

    fun addForm(form: Form) {
        viewModelScope.launch {
            val result = createTicketUseCase(form)
            result.onSuccess { id ->
                Log.d("AddForm", "Form added with ID: $id")
                _eventFlow.emit(UiEvent.NavigateToPayment(id))

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
        _uiState.value = _uiState.value.copy(age = age)
    }

    fun updateSex(sex: Sex) {
        _uiState.value = _uiState.value.copy(sex = sex)
    }

    fun updateDeptId(deptId: String) {
        _uiState.value = _uiState.value.copy(departmentId = deptId)
    }
    fun updateBloodGroup(bloodGroup: String) {
        _uiState.value = _uiState.value.copy(bloodGroup = bloodGroup)
    }
}

sealed class UiEvent {
    data class NavigateToPayment(val formId: String) : UiEvent()
    data class ShowError(val message: String) : UiEvent()
}