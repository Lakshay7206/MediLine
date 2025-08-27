package com.example.mediline.User.data.model

import android.util.Log
import com.example.mediline.User.ui.createTicket.Sex
data class FormEntity(

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
    val ticketStatus: String = "ACTIVE"    // stored as String
)

fun FormEntity.toDomain(): Form {
    val enumSex = try { Sex.valueOf(sex.uppercase()) } catch (e: Exception) { Sex.Other }
    val enumPayment = try { PaymentStatus.valueOf(paymentStatus.uppercase()) } catch (e: Exception) { PaymentStatus.UNPAID }
    val enumTicket  = try { TicketStatus.valueOf(ticketStatus.uppercase().trim()) } catch (e: Exception) { TicketStatus.NULL}

    return Form(

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
        ticketStatus = enumTicket


    )
}


interface TicketRepository {
    suspend fun getTickets(): Result<List<Form>>
}