package com.example.mediline.data.room

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.firebase.firestore.DocumentId

@Entity(tableName = "departments")
data class DepartmentEntity(
    @PrimaryKey
    var id: String = "",
    val name: String = "",
    val description: String = "",
    val doctor: String = "",
    val fees: Int = 0,
    val todayCounter: Int = 0 // <-- new field
) {
    constructor() : this("", "", "", "", 0, 0)
}

