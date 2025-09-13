package com.example.mediline.data.repo

import com.example.mediline.data.model.AdminProfile


import android.net.Uri
import com.example.mediline.data.model.AdminProfileRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject


class AdminProfileRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : AdminProfileRepository {

    override suspend fun loadProfile(): AdminProfile {
        // For practice mode: just return the first admin if exists
        val snapshot = firestore.collection("admins").limit(1).get().await()
        val doc = snapshot.documents.firstOrNull()

        return doc?.let {
            AdminProfile(
                id = it.id,
                name = it.getString("name") ?: "",
                email = it.getString("email") ?: "",
                role = it.getString("role") ?: "",
                imageUrl = it.getString("imageUrl") ?: "https://example.com/default_profile.jpg"
            )
        } ?: AdminProfile(
            imageUrl = "https://example.com/default_profile.jpg"
        )
    }

    override suspend fun saveProfile(profile: AdminProfile) {
        // Generate new document or update existing one
        val docRef = if (profile.id.isNotEmpty()) {
            firestore.collection("admins").document(profile.id)
        } else {
            firestore.collection("admins").document()
        }

        val profileWithId = profile.copy(id = docRef.id)

        val profileData = mapOf(
            "id" to profileWithId.id,
            "name" to profileWithId.name,
            "email" to profileWithId.email,
            "role" to profileWithId.role,
            "imageUrl" to profileWithId.imageUrl.ifEmpty { "https://example.com/default_profile.jpg" }
        )

        docRef.set(profileData).await()
    }

    override suspend fun getAllAdmins(): List<AdminProfile> {
        val snapshot = firestore.collection("admins").get().await()
        return snapshot.documents.map { doc ->
            AdminProfile(
                id = doc.id,
                name = doc.getString("name") ?: "",
                email = doc.getString("email") ?: "",
                role = doc.getString("role") ?: "",
                imageUrl = doc.getString("imageUrl") ?: "https://example.com/default_profile.jpg"
            )
        }
    }
}
