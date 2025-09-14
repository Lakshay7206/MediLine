package com.example.mediline.data.repo

import com.example.mediline.data.model.AdminProfile


import android.net.Uri
import com.example.mediline.data.model.AdminProfileRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject


class AdminProfileRepositoryImpl(
    private val firestore: FirebaseFirestore
) : AdminProfileRepository {

    private val adminsCollection = firestore.collection("admins")

    override suspend fun loadProfile(uid: String): AdminProfile? {
        val snapshot = adminsCollection.document(uid).get().await()
        return if (snapshot.exists()) {
            snapshot.toObject(AdminProfile::class.java)
        } else {
            null
        }
    }

    override suspend fun saveProfile(profile: AdminProfile) {
        adminsCollection.document(profile.uid).set(profile).await()
    }

//    override suspend fun getAllAdmins(): List<AdminProfile> {
//        val snapshot = adminsCollection.get().await()
//        return snapshot.documents.mapNotNull { it.toObject(AdminProfile::class.java) }
//    }
    override  suspend fun getAllAdmins(): Flow<List<AdminProfile>> = callbackFlow {
        val listener = adminsCollection.addSnapshotListener { snapshot, error ->
            if (error != null) {
                close(error) // close flow on error
                return@addSnapshotListener
            }

            val admins = snapshot?.documents
                ?.mapNotNull { it.toObject(AdminProfile::class.java) }
                ?: emptyList()

            trySend(admins).isSuccess
        }

        awaitClose { listener.remove() }
    }


    override suspend fun deleteProfile(uid: String) {
        adminsCollection.document(uid).delete().await()
    }
}
