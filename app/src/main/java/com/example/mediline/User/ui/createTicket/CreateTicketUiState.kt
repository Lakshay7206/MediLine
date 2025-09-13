package com.example.mediline.User.ui.createTicket

import androidx.compose.ui.tooling.data.Group
import com.example.mediline.data.model.Department


data class CreateTicketUiState(
    val name: String="",
    val address: String="",
    val phone: String="",
    val age: String="",
    val sex:Sex=Sex.Male,
    val departmentId: String="",
   val bloodGroup: String?= "",
    val fatherName: String="",
    val errors: Map<String, String?> = emptyMap()
)

enum class Sex {
    Male,
    Female,
    Other
}