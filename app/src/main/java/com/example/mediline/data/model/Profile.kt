package com.example.mediline.data.model


data class AdminProfile(
    val id: String = "",
    val name: String = "",
    val email: String = "",
    val role: String = "",
    val imageUrl: String = ""
)


interface AdminProfileRepository {
    suspend fun loadProfile(): AdminProfile
    suspend fun saveProfile(profile: AdminProfile)
    suspend fun getAllAdmins(): List<AdminProfile>
}