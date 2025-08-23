package com.example.mediline.User.data.model

import kotlinx.coroutines.flow.Flow

data class Department(


    val name: String,
    val description: String ,
    val doctor: String,
    val fees: Int,
    val id: String

)

interface DepartmentRepository {
   fun getDepartments(): Flow<List<Department>>
   suspend fun getDepartmentById(id: String): Department?
}
