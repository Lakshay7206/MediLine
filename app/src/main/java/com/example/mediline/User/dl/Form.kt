package com.example.mediline.User.dl

data class Form (
    val departmentId:Int,
    val userId: String="",
    val opdNo: String,
    val name: String,
    val address: String,
    val age: Int,
    val sex:Sex,
    val timeStamp: String,
    //val bloodGroup: String,

)

enum class Sex {
    Male,
    Female,
    Other
}

interface FormRepository{
    suspend fun addForm(form: Form): Result<String>
}