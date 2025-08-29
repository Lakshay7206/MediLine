package com.example.mediline.data.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface DepartmentDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDepartments(departments: List<DepartmentEntity>)

    @Query("SELECT * FROM departments")
    fun getDepartments(): Flow<List<DepartmentEntity>>

    @Query("DELETE FROM departments")
    suspend fun clearDepartments()

    @Query("SELECT * FROM departments WHERE id = :id LIMIT 1")
    fun getDepartmentById(id: String): Flow<DepartmentEntity?>
}