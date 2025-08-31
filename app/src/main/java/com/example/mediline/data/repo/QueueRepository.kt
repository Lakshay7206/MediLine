package com.example.mediline.data.repo


import android.util.Log
import com.example.mediline.data.model.Form
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import java.util.Calendar
import javax.inject.Inject

interface QueueRepository {
    fun getTodaysTickets(departmentId: String): Flow<List<Form>>


}

class QueueRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : QueueRepository {

    override fun getTodaysTickets(departmentId: String): Flow<List<Form>> = callbackFlow {
        val startOfDay = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.timeInMillis

        val endOfDay = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 23)
            set(Calendar.MINUTE, 59)
            set(Calendar.SECOND, 59)
            set(Calendar.MILLISECOND, 999)
        }.timeInMillis

        Log.d("QueueRepositoryImpl", "Department ID: $departmentId")
        val query = firestore.collection("forms")
            .whereEqualTo("departmentId", departmentId)
            .whereGreaterThanOrEqualTo("timeStamp", startOfDay)
            .whereLessThanOrEqualTo("timeStamp", endOfDay)
            //.orderBy("timeStamp", Query.Direction.ASCENDING)
        Log.d("QueueRepositoryImpl", "Query: $query")
        val subscription = query.addSnapshotListener { snapshot, error ->
            if (error != null) {
                close(error)
                return@addSnapshotListener
            }

            val tickets = snapshot?.documents?.mapNotNull { doc ->
                doc.toObject(Form::class.java)?.copy(id = doc.id)
            } ?: emptyList()

            trySend(tickets)
        }

        awaitClose { subscription.remove() }
    }
}

//class QueueRepositoryImpl(
//    private val firestore: FirebaseFirestore
//) : QueueRepository {
//
//    override suspend fun getQueue(deptId: String): Result<Int> {
//        return try {
//            val snapshot = firestore
//                .collection("queues")
//                .document(deptId)
//                .get()
//                .await()
//
//            if (snapshot.exists()) {
//                val queueLength = snapshot.getLong("queueLength")?.toInt() ?: 0
//                Result.success(queueLength)
//            } else {
//                Result.failure(Exception("Queue document not found for department $deptId"))
//            }
//        } catch (e: Exception) {
//            Result.failure(e)
//        }
//    }
//}
