package com.example.mediline.data.model

import com.example.mediline.User.ui.createTicket.Sex

data class Form (
    val id: String ="",
    val departmentId: String="",
    val userId: String="",
    val opdNo: String="",
    val name: String="",
    val address: String="",
    val phone: String="",
    val age: Int=0,
    val sex: Sex?=Sex.Other,
    val timeStamp: Long= 0,
    val ticketNumber: Long =0,
    val paymentStatus: PaymentStatus=PaymentStatus.UNPAID,
    val ticketStatus: TicketStatus=TicketStatus.ACTIVE,
    val createdBy: String="",     // UID of creator (admin or same as userId)
    val creatorRole: CreatorRole=CreatorRole.NULL,   // "admin" or "user"
    //val bloodGroup: String,

)

enum class CreatorRole{
    ADMIN,
    USER,
    NULL
}
enum class TicketStatus{

   ACTIVE,
    CLOSED,
    EXPIRED,
    CANCELLED,
    SKIPPED,
    NULL,
    SERVING
}
enum class PaymentStatus{
    Paid,
    UNPAID,
    Failed

}
interface FormRepository{
    suspend fun addForm(form: Form): Result<String>


}
interface AdminFormRepository{
    suspend fun addTicketForUser(form: Form, userId: String?): Result<String>
}