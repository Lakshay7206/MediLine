package com.example.mediline.User.data.model

import com.example.mediline.User.ui.createTicket.Sex

data class Form (
    val departmentId: Int,
    val userId: String ="",
    val opdNo: String,
    val name: String,
    val address: String,
    val phone: String,
    val age: Int,
    val sex: Sex,
    val timeStamp: Long,
    //val bloodGroup: String,

)


interface FormRepository{
    suspend fun addForm(form: Form): Result<String>


}