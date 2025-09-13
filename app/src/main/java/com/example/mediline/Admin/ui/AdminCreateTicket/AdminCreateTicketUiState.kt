package com.example.mediline.Admin.ui.AdminCreateTicket

import com.example.mediline.User.ui.createTicket.Sex
import com.example.mediline.data.model.Department

data class AdminTicketUiState(
    val name: String = "",
    val age: String = "",
    val phone: String = "",
    val sex: Sex? = Sex.Male,
    val bloodGroup: String = "",
    val doctor: String ="",
    val fees: String = "",
    val departmentId: String = "",
    val isSubmitting: Boolean = false,
    val errorMessage: String? = null,
    val address: String ="",
    val fatherName: String ="",
    val departments: List<Department> = emptyList(),
    val errors: Map<String, String?> = emptyMap(),
    val successMessage: String? = null // ✅ new field
)


