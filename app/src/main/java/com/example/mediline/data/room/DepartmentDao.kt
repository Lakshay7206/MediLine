package com.example.mediline.data.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface DepartmentDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDepartments(departments: List<DepartmentEntity>)

    // Added: For single insert or update from repository
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDepartment(department: DepartmentEntity)

    // Added: Explicit update (useful if not always using REPLACE for inserts)
    @Update
    suspend fun updateDepartment(department: DepartmentEntity)

    // Added: Specific deletion by ID
    @Query("DELETE FROM departments WHERE id = :id")
    suspend fun deleteDepartmentById(id: String)
    @Query("SELECT * FROM departments")
    fun getDepartments(): Flow<List<DepartmentEntity>>

    @Query("DELETE FROM departments")
    suspend fun clearDepartments()

    @Query("SELECT * FROM departments WHERE id = :id LIMIT 1")
    fun getDepartmentById(id: String): Flow<DepartmentEntity?>
}