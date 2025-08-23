package com.example.mediline.User.data.model

import kotlinx.coroutines.flow.Flow

data class Department(
    val name: String = "",
    val description: String = " ",
    val doctor: String = "",
    val todayCounter:Int=0
)
interface DepartmentRepository {
   fun getDepartments(): Flow<List<Department>>
   suspend fun getDepartmentById(id: String): Department?
}
