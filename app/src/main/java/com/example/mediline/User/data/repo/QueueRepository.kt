package com.example.mediline.User.data.repo

import com.example.mediline.User.data.model.QueueRepository
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class QueueRepositoryImpl(
    private val firestore: FirebaseFirestore
) : QueueRepository {

    override suspend fun getQueue(deptId: String): Result<Int> {
        return try {
            val snapshot = firestore
                .collection("queues")
                .document(deptId)
                .get()
                .await()

            if (snapshot.exists()) {
                val queueLength = snapshot.getLong("queueLength")?.toInt() ?: 0
                Result.success(queueLength)
            } else {
                Result.failure(Exception("Queue document not found for department $deptId"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
