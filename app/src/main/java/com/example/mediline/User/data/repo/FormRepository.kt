package com.example.mediline.User.data.repo

import com.example.mediline.User.dl.Form
import com.example.mediline.User.dl.FormRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.tasks.await
import kotlin.coroutines.resume

class FormRepositoryImpl(private val firestore: FirebaseFirestore,
                         private val auth: FirebaseAuth
) : FormRepository {
    override suspend fun addForm(form: Form): Result<String> {
        return try {
            val currentUser =
                auth.currentUser ?: return Result.failure(Exception("User not logged in"))
            val formWithUser = form.copy(userId = currentUser.uid)
            val docRef = firestore.collection("forms").add(formWithUser).await()
            Result.success(docRef.id)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}


