package com.example.mediline.User.data.repo

import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject
import com.example.mediline.User.data.model.Department
import com.example.mediline.User.data.model.DepartmentRepository
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class DepartmentRepositoryImpl @Inject constructor(
        private val db: FirebaseFirestore
) : DepartmentRepository {

    override fun getDepartments(): Flow<List<Department>> = callbackFlow {
        val ref = db.collection("departments")
        val listener = ref.addSnapshotListener { snapshot, error ->
            if (error != null) {
                close(error)
                return@addSnapshotListener
            }

            val list = snapshot?.documents?.mapNotNull { doc ->
                doc.toObject(Department::class.java)?.copy(
                    id = doc.id  // ✅ override with Firestore docId
                )
            } ?: emptyList()

            trySend(list)
        }
        awaitClose { listener.remove() }
    }

    override suspend fun getDepartmentById(id: String): Department? {
        val snap = db.collection("departments").document(id).get().await()
        return snap.toObject(Department::class.java)?.copy( id = snap.id )
    }
}

