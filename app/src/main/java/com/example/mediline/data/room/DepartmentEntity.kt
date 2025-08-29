package com.example.mediline.data.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "departments")
data class DepartmentEntity(
    @PrimaryKey val id: String = "",
    val name: String="",
    val description: String="",
    val doctor: String="",
    val fees: Int=0,

)