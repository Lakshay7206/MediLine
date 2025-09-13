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


fun DepartmentEntity.toDomain(): Department {
    return Department(
        id = id,
        name = name,
        description = description,
        doctor = doctor,
        fees = fees
    )
}

fun Department.toEntity(): DepartmentEntity {
    return DepartmentEntity(
        id = id,
        name = name,
        description = description,
        doctor = doctor,
        fees = fees
    )
}


interface DepartmentRepository {
    fun getDepartments(): Flow<List<DepartmentEntity>>
    suspend fun createDepartment(department: DepartmentEntity): Result<String>
    suspend fun updateDepartment(department: DepartmentEntity): Result<Unit>
    suspend fun deleteDepartment(departmentId: String): Result<Unit>
    suspend fun syncDepartmentsFromFirestore(): Result<Unit>
    suspend fun getDepartmentById(id: String): Flow<DepartmentEntity?>
}
//interface DepartmentRepository {
//   suspend fun getDepartments(): Flow<List<Department>>
//   suspend fun getDepartmentById(id: String): Department?
//
//    suspend fun createDepartment(department: Department): Result<String>
//
//}
