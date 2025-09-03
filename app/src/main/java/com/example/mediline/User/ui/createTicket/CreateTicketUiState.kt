package com.example.mediline.User.ui.createTicket

import androidx.compose.ui.tooling.data.Group
import com.example.mediline.data.model.Department


data class CreateTicketUiState(
    val name: String="",
    val address: String="",
    val phone: String="",
    val age: Int=0,
    val sex:Sex=Sex.Male,
    val departmentId: String="",
   val bloodGroup: String?= ""
)

enum class Sex {
    Male,
    Female,
    Other
}