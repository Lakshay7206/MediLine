package com.example.mediline.data.repo

import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject
import com.example.mediline.data.model.Department
import com.example.mediline.data.model.DepartmentRepository
import com.example.mediline.data.room.DepartmentDao
import com.example.mediline.data.room.DepartmentEntity

import kotlinx.coroutines.tasks.await



import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await

class DepartmentRepositoryImpl @Inject constructor(
    private val firebaseDb: FirebaseFirestore,
    private val departmentDao: DepartmentDao
) : DepartmentRepository {

    override  fun getDepartments(): Flow<List<DepartmentEntity>> = flow {
        val localData = departmentDao.getDepartments().firstOrNull() // Get the current Room data
        if (!localData.isNullOrEmpty()) {
            emit(localData) // Return Room data
        } else {
            // Fetch from backend
            val snapshot = firebaseDb.collection("departments").get().await()
            val departments = snapshot.documents.mapNotNull { it.toObject(DepartmentEntity::class.java)?.copy(id = it.id) }
            departmentDao.insertDepartments(departments) // Cache in Room
            emit(departments)
        }
    }

    override suspend fun createDepartment(department: DepartmentEntity) {
        firebaseDb.collection("departments")
            .document(department.id)
            .set(department)
            .await()

        departmentDao.insertDepartments(listOf(department))
    }

    override suspend fun syncDepartments() {
        val snapshot = firebaseDb.collection("departments").get().await()
        val remoteDepartments = snapshot.map { it.toObject(DepartmentEntity::class.java) }
        departmentDao.insertDepartments(remoteDepartments)
    }

    override suspend fun getDepartmentById(id: String): Flow<DepartmentEntity?> {
            return departmentDao.getDepartmentById(id)

    }

}

//class DepartmentRepositoryImpl @Inject constructor(
//        private val db: FirebaseFirestore
//) : DepartmentRepository {

//    override suspend fun getDepartments(): Flow<List<Department>> = callbackFlow {
//        val ref = db.collection("departments")
//        val listener = ref.addSnapshotListener { snapshot, error ->
//            if (error != null) {
//                close(error)
//                return@addSnapshotListener
//            }
//
//            val list = snapshot?.documents?.mapNotNull { doc ->
//                doc.toObject(Department::class.java)?.copy(
//                    id = doc.id  // ✅ override with Firestore docId
//                )
//            } ?: emptyList()
//
//            trySend(list)
//        }
//        awaitClose { listener.remove() }
//    }
//
//    override suspend fun getDepartmentById(id: String): Department? {
//        val snap = db.collection("departments").document(id).get().await()
//        return snap.toObject(Department::class.java)?.copy( id = snap.id )
//    }

//    override suspend fun createDepartment(department: Department): Result<String> {
//        return try {
//            val ref = db.collection("departments").add(department).await()
//            Result.success(ref.id)
//        } catch (e: Exception) {
//            Result.failure(e)
//        }
//    }
//}

