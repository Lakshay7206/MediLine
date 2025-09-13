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
import com.example.mediline.data.model.toDomain
import com.example.mediline.dl.AdminAddFormUseCase
import com.example.mediline.dl.GetDepartmentsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AdminCreateTicketViewModel @Inject constructor(
    private val adminAddFormUseCase: AdminAddFormUseCase,
    private val getDepartmentsUseCase: GetDepartmentsUseCase,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        AdminTicketUiState(
            name = savedStateHandle.get<String>("name") ?: "",
            fatherName = savedStateHandle.get<String>("fatherName") ?: "",
            age = savedStateHandle.get<String>("age") ?: "",
            phone = savedStateHandle.get<String>("phone") ?: "",
            departmentId = savedStateHandle.get<String>("departmentId") ?: "",
            address = savedStateHandle.get<String>("address") ?: "",
            sex = savedStateHandle.get<Sex>("sex"),
            departments = emptyList()
        )
    )
    val uiState: StateFlow<AdminTicketUiState> = _uiState.asStateFlow()

    init {
        // ✅ Fetch departments once when ViewModel is created
        getDepartments()
    }

    // ---------------- Form field updates ----------------

    fun onNameChange(name: String) {
        _uiState.update { it.copy(name = name) }
        savedStateHandle["name"] = name
    }

    fun onFatherNameChange(name: String) {
        _uiState.update { it.copy(fatherName = name) }
        savedStateHandle["fatherName"] = name
    }

    fun onAgeChange(age: String) {
        _uiState.update { it.copy(age = age) }
        savedStateHandle["age"] = age
    }

    fun onPhoneChange(phone: String) {
        _uiState.update { it.copy(phone = phone) }
        savedStateHandle["phone"] = phone
    }

    fun onDepartmentChange(departmentId: String) {
        _uiState.update { it.copy(departmentId = departmentId) }
        savedStateHandle["departmentId"] = departmentId
    }

    fun onAddressChange(address: String) {
        _uiState.update { it.copy(address = address) }
        savedStateHandle["address"] = address
    }

    fun onBloodGroupChange(group: String) {
        _uiState.update { it.copy(bloodGroup = group) }
        savedStateHandle["bloodGroup"] = group
    }

    fun onSexChange(sex: String) {
        val sexEnum = try {
            Sex.valueOf(sex)
        } catch (e: IllegalArgumentException) {
            Sex.Other
        }
        _uiState.update { it.copy(sex = sexEnum) }
        savedStateHandle["sex"] = sexEnum.name
    }

    fun clearSuccessMessage() {
        _uiState.update { it.copy(successMessage = null) }
    }

    // ---------------- Departments ----------------

    private fun getDepartments() {
        viewModelScope.launch {
            getDepartmentsUseCase().collect { entities ->
                _uiState.update {
                    it.copy(departments = entities.map { entity -> entity.toDomain() })
                }
            }
        }
    }

    //.....................................................

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
        if (state.fatherName.trim().isBlank()){
            errors["fatherName"] = "Father's name is required"

        }

        _uiState.value = _uiState.value.copy(errors = errors) // push errors to state
        return errors.isEmpty()
    }

    // ---------------- Ticket submission ----------------

    fun submitTicket(userId: String) {
        if (!validate()) return
        viewModelScope.launch {
            _uiState.update { it.copy(isSubmitting = true, errorMessage = null) }

            val form = Form(
                id = "",
                name = uiState.value.name,
                fatherName = uiState.value.fatherName,
                age = uiState.value.age.toIntOrNull() ?: 0,
                phone = uiState.value.phone,
                departmentId = uiState.value.departmentId,
                address = uiState.value.address,
                sex = uiState.value.sex,
                createdBy = "",
                userId = userId,
                ticketNumber = 0,
                timeStamp = System.currentTimeMillis(),
                opdNo = "",
                paymentStatus = PaymentStatus.UNPAID,
                ticketStatus = TicketStatus.ACTIVE,
                creatorRole = CreatorRole.ADMIN
            )

            val result = adminAddFormUseCase(form, userId)
            result.fold(
                onSuccess = {
                    // ✅ Reset only form fields, keep departments intact
                    _uiState.update { state ->
                        state.copy(
                            name = "",
                            fatherName = "",
                            age = "",
                            phone = "",
                            departmentId = "",
                            address = "",
                            sex = null,
                            isSubmitting = false,
                            errorMessage = "Failed to create ticket",
                            successMessage = "Ticket created successfully ✅" // 👈 add this

                            // departments stay unchanged
                        )
                    }
                    // clear SavedStateHandle for form fields only
                    savedStateHandle["name"] = ""
                    savedStateHandle["fatherName"] = ""
                    savedStateHandle["age"] = ""
                    savedStateHandle["phone"] = ""
                    savedStateHandle["departmentId"] = ""
                    savedStateHandle["address"] = ""
                    savedStateHandle["sex"] = null
                    savedStateHandle["bloodGroup"] = null
                },
                onFailure = { e ->
                    _uiState.update { it.copy(isSubmitting = false, errorMessage = e.message) }
                }
            )
        }
    }
}












//@HiltViewModel
//class AdminCreateTicketViewModel @Inject constructor(
//    private val adminAddFormUseCase: AdminAddFormUseCase,
//    private val getDepartmentsUseCase: GetDepartmentsUseCase,
//    private val savedStateHandle: SavedStateHandle
//) : ViewModel() {
//
//    private val _uiState = MutableStateFlow(
//        AdminTicketUiState(
//            name = savedStateHandle.get<String>("name") ?: "",
//            fatherName = savedStateHandle.get<String>("fatherName") ?: "",
//            age = savedStateHandle.get<String>("age") ?: "",
//            phone = savedStateHandle.get<String>("phone") ?: "",
//            departmentId = savedStateHandle.get<String>("departmentId") ?: "",
//            address = savedStateHandle.get<String>("address") ?: "",
//            sex = savedStateHandle.get<Sex>("sex")
//        )
//    )
//    val uiState: StateFlow<AdminTicketUiState> = _uiState
//
//    init {
//        getDepartments()
//    }
//
//    fun getDepartments() {
//        viewModelScope.launch {
//            getDepartmentsUseCase().collect { entities ->
//                _uiState.update {
//                    it.copy(
//                        departments = entities.map { it.toDomain() }
//                    )
//                }
//            }
//        }
//    }
//
//    fun onNameChange(name: String) {
//        _uiState.value = _uiState.value.copy(name = name)
//        savedStateHandle["name"] = name
//    }
//
//    fun onFatherNameChange(name: String) {
//        _uiState.value = _uiState.value.copy(fatherName = name)
//        savedStateHandle["fatherName"] = name
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
//    fun onAddressChange(address: String) {
//        _uiState.value = _uiState.value.copy(address = address)
//        savedStateHandle["address"] = address
//    }
//
//    fun onBloodGroupChange(group: String) {
//        _uiState.value = _uiState.value.copy(bloodGroup = group)
//        savedStateHandle["bloodGroup"] = group
//    }
//
//    fun onSexChange(sex: String) {
//        val sexEnum = try {
//            Sex.valueOf(sex) // convert String to enum
//        } catch (e: IllegalArgumentException) {
//            Sex.Other // fallback in case of invalid value
//        }
//
//        _uiState.value = _uiState.value.copy(sex = sexEnum)
//        savedStateHandle["sex"] = sexEnum.name // save as String
//    }
//
//    fun submitTicket(userId: String) {
//        viewModelScope.launch {
//            _uiState.value = _uiState.value.copy(isSubmitting = true, errorMessage = null)
//
//            val form = Form(
//                id = "",
//                name = uiState.value.name,
//                age = uiState.value.age.toIntOrNull() ?: 0,
//                phone = uiState.value.phone,
//                departmentId = "1",
//                createdBy = "",
//                userId = userId,
//                ticketNumber = 0, // handled in backend
//                timeStamp = System.currentTimeMillis(),
//                opdNo = "",
//                address = uiState.value.address,
//                sex = uiState.value.sex,
//                paymentStatus = PaymentStatus.UNPAID,
//                ticketStatus = TicketStatus.NULL,
//                creatorRole = CreatorRole.ADMIN,
//                fatherName = uiState.value.fatherName
//            )
//
//            val result = adminAddFormUseCase(form, userId)
//            result.fold(
//                onSuccess = {
//                    _uiState.value = AdminTicketUiState() // reset state
//                    Log.d("AdminCreateTicketViewModel", "Ticket submitted ")
//                    // clear saved state safely
//                    savedStateHandle.keys().forEach { key -> savedStateHandle.remove<Any>(key) }
//                },
//                onFailure = { e ->
//                    _uiState.value =
//                        _uiState.value.copy(isSubmitting = false, errorMessage = e.message)
//                }
//            )
//        }
//    }
//}


