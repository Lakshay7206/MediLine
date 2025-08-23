package com.example.mediline.User.data.model

import com.example.mediline.User.ui.createTicket.Sex

data class Form (
    val departmentId: String,
    val userId: String ="",
    val opdNo: String,
    val name: String,
    val address: String,
    val phone: String,
    val age: Int,
    val sex: Sex,
    val timeStamp: Long,
    val ticketNumber:Long=0,
    val paymentStatus: PaymentStatus=PaymentStatus.Unpaid,
    val ticketStatus: TicketStatus= TicketStatus.Open
    //val bloodGroup: String,

)
enum class TicketStatus{
    Closed,
    Waiting,
    Open
}
enum class PaymentStatus{
    Paid,
    Unpaid,
    Failed

}
interface FormRepository{
    suspend fun addForm(form: Form): Result<String>


}