package com.example.mediline.data.room

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.firebase.firestore.DocumentId

@Entity(tableName = "departments")
data class DepartmentEntity(
    @PrimaryKey
    @DocumentId
    var id: String = "",
    val name: String="",
    val description: String="",
    val doctor: String="",
    val fees: Int=0,

) {
    // Firestore often requires a no-argument constructor for deserialization
    constructor() : this("", "", "", "", 0)
}

