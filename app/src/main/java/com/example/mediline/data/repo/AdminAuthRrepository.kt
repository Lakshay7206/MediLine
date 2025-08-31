package com.example.mediline.data.repo

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

interface AdminAuthRepository {
    suspend fun loginAdmin(email: String, password: String): Result<Unit>
}

class AdminAuthRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) : AdminAuthRepository {

    override suspend fun loginAdmin(email: String, password: String): Result<Unit> {
        return try {
            val authResult = firebaseAuth.signInWithEmailAndPassword(email, password).await()
            val user = authResult.user ?: throw Exception("User not found")

            // Refresh token to get latest custom claims
            val idTokenResult = user.getIdToken(true).await()
            val claims = idTokenResult.claims
            val role = claims["role"] as? String

            if (role != "admin") throw Exception("Not an admin")

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}


//class AdminAuthRepositoryImpl @Inject constructor(
//    private val firebaseAuth: FirebaseAuth,
//    private val firestore: FirebaseFirestore
//) : AdminAuthRepository {
//
//    override suspend fun loginAdmin(email: String, password: String): Result<Unit> {
//        return try {
//            val authResult = firebaseAuth.signInWithEmailAndPassword(email, password).await()
//            val uid = authResult.user?.uid ?: throw Exception("User not found")
//
//            // Optional: check role in Firestore
//            val doc = firestore.collection("admins").document(uid).get().await()
//            if (!doc.exists()) throw Exception("Not an admin")
//
//            Result.success(Unit)
//        } catch (e: Exception) {
//            Result.failure(e)
//        }
//    }
//}

//suspend fun loginAdmin(email: String, password: String): Boolean {
//    return try {
//        val authResult = Firebase.auth.signInWithEmailAndPassword(email, password).await()
//        val uid = authResult.user?.uid ?: return false
//
//        val userDoc = Firebase.firestore.collection("users")
//            .document(uid)
//            .get()
//            .await()
//
//        val role = userDoc.getString("role")
//        role == "admin"
//    } catch (e: Exception) {
//        e.printStackTrace()
//        false
//    }
//}
