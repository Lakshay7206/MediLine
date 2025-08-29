package com.example.mediline.data.model

import com.example.mediline.User.ui.createTicket.Sex
data class FormEntity(
    val id: String = "",
   val departmentId: String ="",
    val userId: String = "",
    val opdNo: String = "",
    val name: String = "",
    val address: String = "",
    val phone: String = "",
    val age: Int = 0,
    val sex: String = "",   // stored as String
    val timeStamp: Long = 0,
    val ticketNumber: Long = 0,
    val paymentStatus: String = "UNPAID",  // stored as String
    val ticketStatus: String = "ACTIVE" ,
    val createdBy: String = "",
    val creatorRole: String=""
)

fun FormEntity.toDomain(): Form {
    val enumSex = try { Sex.valueOf(sex.uppercase()) } catch (e: Exception) { Sex.Other }
    val enumPayment = try { PaymentStatus.valueOf(paymentStatus.uppercase()) } catch (e: Exception) { PaymentStatus.UNPAID }
    val enumTicket  = try { TicketStatus.valueOf(ticketStatus.uppercase().trim()) } catch (e: Exception) { TicketStatus.NULL}
    val enumCreator=try{CreatorRole.valueOf(creatorRole.uppercase().trim())}catch (e:Exception){CreatorRole.NULL}

    return Form(
        id = id,
        userId = userId,
        opdNo = opdNo,
        name = name,
        address = address,
        phone = phone,
        age = age,
        sex = enumSex,
        timeStamp = timeStamp,
        ticketNumber = ticketNumber,
        paymentStatus = enumPayment,
        ticketStatus = enumTicket,
        departmentId = departmentId,
        createdBy =userId,
        creatorRole =enumCreator
    )
}


interface TicketRepository {
    suspend fun getTickets(): Result<List<Form>>
}