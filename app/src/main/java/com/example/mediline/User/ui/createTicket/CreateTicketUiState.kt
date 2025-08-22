package com.example.mediline.User.ui.createTicket



data class CreateTicketUiState(
    val name: String="",
    val address: String="",
    val phone: String="",
    val age: Int=0,
    val sex:Sex=Sex.Male
)

enum class Sex {
    Male,
    Female,
    Other
}