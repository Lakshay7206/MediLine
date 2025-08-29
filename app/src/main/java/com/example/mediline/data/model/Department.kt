package com.example.mediline.data.model

import com.example.mediline.data.room.DepartmentEntity
import kotlinx.coroutines.flow.Flow

data class Department(
    val name: String="",
    val description: String="",
    val doctor: String="",
    val fees: Int=0,
    val id: String=""

)

interface DepartmentRepository {
    fun getDepartments(): Flow<List<DepartmentEntity>>
    suspend fun createDepartment(department: DepartmentEntity)
    suspend fun syncDepartments()
    suspend fun getDepartmentById(id: String): Flow<DepartmentEntity?>
}
//interface DepartmentRepository {
//   suspend fun getDepartments(): Flow<List<Department>>
//   suspend fun getDepartmentById(id: String): Department?
//
//    suspend fun createDepartment(department: Department): Result<String>
//
//}
