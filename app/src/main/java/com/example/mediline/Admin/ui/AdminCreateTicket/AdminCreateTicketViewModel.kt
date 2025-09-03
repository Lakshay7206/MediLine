package com.example.mediline.Admin.ui.AdminCreateTicket

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mediline.User.ui.createTicket.Sex
import com.example.mediline.data.model.CreatorRole
import com.example.mediline.data.model.Form
import com.example.mediline.data.model.PaymentStatus
import com.example.mediline.data.model.TicketStatus
import com.example.mediline.dl.AdminAddFormUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AdminCreateTicketViewModel @Inject constructor(
    private val adminAddFormUseCase: AdminAddFormUseCase,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        AdminTicketUiState(
            name = savedStateHandle.get<String>("name") ?: "",
            age = savedStateHandle.get<String>("age") ?: "",
            phone = savedStateHandle.get<String>("phone") ?: "",
            departmentId = savedStateHandle.get<String>("departmentId") ?: "",
            address = savedStateHandle.get<String>("address") ?: "",
            sex = savedStateHandle.get<Sex>("sex")
        )
    )
    val uiState: StateFlow<AdminTicketUiState> = _uiState

    fun onNameChange(name: String) {
        _uiState.value = _uiState.value.copy(name = name)
        savedStateHandle["name"] = name
    }

    fun onAgeChange(age: String) {
        _uiState.value = _uiState.value.copy(age = age)
        savedStateHandle["age"] = age
    }

    fun onPhoneChange(phone: String) {
        _uiState.value = _uiState.value.copy(phone = phone)
        savedStateHandle["phone"] = phone
    }

    fun onDepartmentChange(departmentId: String) {
        _uiState.value = _uiState.value.copy(departmentId = departmentId)
        savedStateHandle["departmentId"] = departmentId
    }

    fun onAddressChange(address: String) {
        _uiState.value = _uiState.value.copy(address = address)
        savedStateHandle["address"] = address
    }
    fun onBloodGroupChange(group: String) {
        _uiState.value = _uiState.value.copy(bloodGroup =group)
        savedStateHandle[ "bloodGroup" ] = group
    }

    fun onSexChange(sex: String) {
        val sexEnum = try {
            Sex.valueOf(sex) // convert String to enum
        } catch (e: IllegalArgumentException) {
            Sex.Other // fallback in case of invalid value
        }

        _uiState.value = _uiState.value.copy(sex = sexEnum)
        savedStateHandle["sex"] = sexEnum.name // save as String
    }


    fun submitTicket(userId: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isSubmitting = true, errorMessage = null)

            val form = Form(
                id = "",
                name = uiState.value.name,
                age = uiState.value.age.toIntOrNull() ?: 0,
                phone = uiState.value.phone,
                departmentId ="1",
                createdBy = "",
                userId = userId,
                ticketNumber = 0, // handled in backend
                timeStamp = System.currentTimeMillis(),
                opdNo = "",
                address = uiState.value.address,
                sex = uiState.value.sex,
                paymentStatus = PaymentStatus.UNPAID,
                ticketStatus = TicketStatus.NULL,
                creatorRole = CreatorRole.ADMIN,
            )

            val result = adminAddFormUseCase(form, userId)
            result.fold(
                onSuccess = {
                    _uiState.value = AdminTicketUiState() // reset state
                    Log.d("AdminCreateTicketViewModel", "Ticket submitted ")
                    // clear saved state safely
                    savedStateHandle.keys().forEach { key -> savedStateHandle.remove<Any>(key) }
                },
                onFailure = { e ->
                    _uiState.value = _uiState.value.copy(isSubmitting = false, errorMessage = e.message)
                }
            )
        }
    }
}

//@HiltViewModel
//class AdminCreateTicketViewModel @Inject constructor(
//    private val adminAddFormUseCase: AdminAddFormUseCase,
//    private val savedStateHandle: SavedStateHandle
//) : ViewModel() {
//
//    // Initialize state from SavedStateHandle if available
//    private val _uiState = MutableStateFlow(
//        AdminTicketUiState(
//            name = savedStateHandle.get<String>("name") ?: "",
//            age = savedStateHandle.get<String>("age") ?: "",
//            phone = savedStateHandle.get<String>("phone") ?: "",
//            departmentId = savedStateHandle.get<String>("departmentId") ?: ""
//        )
//    )
//    val uiState: StateFlow<AdminTicketUiState> = _uiState
//
//    // Update form fields and save in SavedStateHandle
//    fun onNameChange(name: String) {
//        _uiState.value = _uiState.value.copy(name = name)
//        savedStateHandle["name"] = name
//    }
//
//    fun onAgeChange(age: String) {
//        _uiState.value = _uiState.value.copy(age = age)
//        savedStateHandle["age"] = age
//    }
//
//    fun onPhoneChange(phone: String) {
//        _uiState.value = _uiState.value.copy(phone = phone)
//        savedStateHandle["phone"] = phone
//    }
//
//    fun onDepartmentChange(departmentId: String) {
//        _uiState.value = _uiState.value.copy(departmentId = departmentId)
//        savedStateHandle["departmentId"] = departmentId
//    }
//
//    // Submit ticket
//    fun submitTicket(userId: String?) {
//        viewModelScope.launch {
//            _uiState.value = _uiState.value.copy(isSubmitting = true, errorMessage = null)
//
//            val form = Form(
//                id = "",
//                name = uiState.value.name,
//                age = uiState.value.age.toInt(),
//                phone = uiState.value.phone,
//                departmentId = uiState.value.departmentId,
//                createdBy = "",
//                userId = "",
//                ticketNumber = 0,
//                timeStamp = System.currentTimeMillis(),
//                opdNo ="",
//                address = uiState.value.address,
//                sex = uiState.value.sex,
//                paymentStatus = PaymentStatus.UNPAID,
//                ticketStatus = TicketStatus.NULL,
//                creatorRole = CreatorRole.ADMIN,
//            )
//
//            val result = adminAddFormUseCase(form, userId)
//            result.fold(
//                onSuccess = {
//                    _uiState.value = AdminTicketUiState() // reset state after success
//                    savedStateHandle.keys().forEach { savedStateHandle.remove<String>(it) } // clear saved state
//                },
//                onFailure = { e ->
//                    _uiState.value = _uiState.value.copy(isSubmitting = false, errorMessage = e.message)
//                }
//            )
//        }
//    }
//}
