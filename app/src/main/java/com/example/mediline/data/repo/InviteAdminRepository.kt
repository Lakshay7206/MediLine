package com.example.mediline.data.repo

import com.example.mediline.data.api.AcceptInviteRequest
import com.example.mediline.data.api.BackendApi
import com.example.mediline.data.api.InviteRequest
import com.example.mediline.data.model.AdminProfile
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject


interface AdminRepository {
    suspend fun sendInvite(email: String): Result<Unit>
    suspend fun acceptInvite(token: String, password: String): Result<Unit>

    suspend fun deleteAdmin(adminId: String):AdminProfile
}
class AdminRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val backendApi: BackendApi
) : AdminRepository {



    override suspend fun sendInvite(email: String): Result<Unit> {
        return try {
            backendApi.sendInvite(InviteRequest(email))
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun acceptInvite(token: String, password: String): Result<Unit> {
        return try {
            backendApi.acceptInvite(AcceptInviteRequest(token, password))
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    override suspend fun deleteAdmin(adminId: String): AdminProfile {
        val docRef = firestore.collection("admins").document(adminId)
        val snapshot = docRef.get().await()

        if (!snapshot.exists()) {
            throw Exception("Admin not found")
        }

        val deletedAdmin = AdminProfile(
            uid = snapshot.id,
           // name = snapshot.getString("name") ?: "",
            email = snapshot.getString("email") ?: "",
            role = snapshot.getString("role") ?: "",
            //imageUrl = snapshot.getString("imageUrl") ?: "https://example.com/default_profile.jpg"
        )

        // Now delete the document
        docRef.delete().await()

        return deletedAdmin
    }
}
