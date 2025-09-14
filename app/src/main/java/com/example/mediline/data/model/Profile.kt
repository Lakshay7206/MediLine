package com.example.mediline.data.model

import com.google.firebase.Timestamp
import kotlinx.coroutines.flow.Flow


data class AdminProfile(
    val uid: String = "",
    val email: String = "",
    val role: String = "admin",
    val createdAt: Timestamp = Timestamp.now()
)

interface AdminProfileRepository {
    suspend fun loadProfile(uid: String): AdminProfile?
    suspend fun saveProfile(profile: AdminProfile)
    suspend fun getAllAdmins(): Flow<List<AdminProfile>>
    suspend fun deleteProfile(uid: String)
}
