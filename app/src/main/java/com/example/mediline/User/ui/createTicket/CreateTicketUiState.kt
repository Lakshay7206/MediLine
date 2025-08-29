package com.example.mediline.User.ui.createTicket

import com.example.mediline.data.model.Department


data class CreateTicketUiState(
    val name: String="",
    val address: String="",
    val phone: String="",
    val age: Int=0,
    val sex:Sex=Sex.Male,
    val departmentId: String=""
)

enum class Sex {
    Male,
    Female,
    Other
}