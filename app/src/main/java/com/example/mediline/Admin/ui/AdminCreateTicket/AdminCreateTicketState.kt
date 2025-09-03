package com.example.mediline.Admin.ui.AdminCreateTicket

import com.example.mediline.User.ui.createTicket.Sex

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
)
