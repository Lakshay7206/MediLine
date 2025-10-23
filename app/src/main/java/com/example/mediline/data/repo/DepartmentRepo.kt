package com.example.mediline.data.repo

import android.util.Log
import androidx.compose.animation.core.copy
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
// DepartmentRepositoryImpl.kt
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.withContext
import java.util.UUID

// DepartmentRepositoryImpl.kt
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.flow.map

class DepartmentRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore, // Renamed for clarity from firebaseDb
    private val departmentDao: DepartmentDao
) : DepartmentRepository {

    private val departmentsCollection = firestore.collection("departments")

    // This Flow now directly returns data from Room.
    // Syncing is an explicit action.
    override fun getDepartments(): Flow<List<DepartmentEntity>> {
        return departmentDao.getDepartments()
    }

    override suspend fun getDepartmentById(id: String): Flow<DepartmentEntity?> {
        return departmentDao.getDepartmentById(id)
    }

    override suspend fun createDepartment(department: DepartmentEntity): Result<String> = withContext(Dispatchers.IO) {
        try {
            // If ID is blank, Firestore will generate one. We capture it.
            val docRef = if (department.id.isBlank()) {
                departmentsCollection.document()
            } else {
                departmentsCollection.document(department.id)
            }
            val departmentToCreate = department.copy(id = docRef.id)

            docRef.set(departmentToCreate).await()
            departmentDao.insertDepartment(departmentToCreate) // Insert into Room
            Result.success(departmentToCreate.id)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateDepartment(department: DepartmentEntity): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            if (department.id.isBlank()) {
                return@withContext Result.failure(IllegalArgumentException("Department ID cannot be blank for update."))
            }
            departmentsCollection.document(department.id).set(department).await() // `set` effectively updates
            departmentDao.updateDepartment(department) // Update in Room
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun deleteDepartment(departmentId: String): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            if (departmentId.isBlank()) {
                return@withContext Result.failure(IllegalArgumentException("Department ID cannot be blank for delete."))
            }
            departmentsCollection.document(departmentId).delete().await()
            departmentDao.deleteDepartmentById(departmentId) // Delete from Room
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Renamed from syncDepartments for clarity of direction
     override suspend fun syncDepartmentsFromFirestore(): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            val snapshot = departmentsCollection.get().await()
//            val remoteDepartments = snapshot.documents.mapNotNull { document ->
//                document.toObject<DepartmentEntity>()?.copy(id = document.id) // Ensure ID is mapped
//            }
            val remoteDepartments = snapshot.documents.mapNotNull { doc ->
                try {
                    DepartmentEntity(
                        id = doc.id,
                        name = doc.getString("name") ?: "",
                        description = doc.getString("description") ?: "",
                        doctor = doc.getString("doctor") ?: "",
                        fees = doc.getLong("fees")?.toInt() ?: 0,
                        todayCounter = doc.getLong("todayCounter")?.toInt() ?: 0
                    )
                } catch (e: Exception) {
                    Log.e("DeptRepo", "Error mapping document ${doc.id}: ${e.message}")
                    null
                }
            }
            departmentDao.insertDepartments(remoteDepartments)

            // Optional: Clear local DB first if you want a complete overwrite strategy
            // departmentDao.clearDepartments()
            departmentDao.insertDepartments(remoteDepartments) // Bulk insert/replace in Room
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e("DeptRepo", "Error syncing departments: ${e.message}")
            Result.failure(e)
        }
    }
}


//class DepartmentRepositoryImpl @Inject constructor(
//    private val firebaseDb: FirebaseFirestore,
//    private val departmentDao: DepartmentDao
//) : DepartmentRepository {
//
//
//    override  fun getDepartments(): Flow<List<DepartmentEntity>> = flow {
//        val localData = departmentDao.getDepartments().firstOrNull() // Get the current Room data
//        if (!localData.isNullOrEmpty()) {
//            emit(localData) // Return Room data
//        } else {
//            // Fetch from backend
//            val snapshot = firebaseDb.collection("departments").get().await()
//            val departments = snapshot.documents.mapNotNull { it.toObject(DepartmentEntity::class.java)?.copy(id = it.id) }
//            departmentDao.insertDepartments(departments) // Cache in Room
//            emit(departments)
//        }
//    }
//
//    override suspend fun createDepartment(department: DepartmentEntity) {
//        firebaseDb.collection("departments")
//            .document(department.id)
//            .set(department)
//            .await()
//
//        departmentDao.insertDepartments(listOf(department))
//    }
//
//    override suspend fun syncDepartments() {
//        val snapshot = firebaseDb.collection("departments").get().await()
//        val remoteDepartments = snapshot.map { it.toObject(DepartmentEntity::class.java) }
//        departmentDao.insertDepartments(remoteDepartments)
//    }
//
//    override suspend fun getDepartmentById(id: String): Flow<DepartmentEntity?> {
//            return departmentDao.getDepartmentById(id)
//
//    }
//
//}





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

