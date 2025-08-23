package com.example.mediline.User.data.repo

import com.example.mediline.User.data.model.Form
import com.example.mediline.User.data.model.FormRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
class FormRepositoryImpl(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
) : FormRepository {

    override suspend fun addForm(form: Form): Result<String> {
        return try {
            // Ensure user is logged in
            val currentUser = auth.currentUser ?: return Result.failure(Exception("User not logged in"))

            // Reference to the department document (where we store todayCounter)
            val deptRef = firestore.collection("departments")
                .document(form.departmentId)

            // Run Firestore transaction
            val ticketNo = firestore.runTransaction { transaction ->
                val snapshot = transaction.get(deptRef)
                val currentCounter = snapshot.getLong("todayCounter") ?: 0
                val newCounter = currentCounter + 1

                // Update today's counter
                transaction.update(deptRef, "todayCounter", newCounter)

                // Create form with ticketNumber and userId
                val formWithTicket = form.copy(
                    userId = currentUser.uid,
                    ticketNumber = newCounter,
                    timeStamp = System.currentTimeMillis()
                )
                firestore.collection("forms").add(formWithTicket)
//
                // Add form to "forms" collection
//                transaction.set(firestore.collection("forms").document(), formWithTicket)

                newCounter
            }.await()
            // Converts Task<Long> to suspend-friendly value

            Result.success("Ticket created! No: $ticketNo")
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

//class FormRepositoryImpl(private val firestore: FirebaseFirestore,
//                         private val auth: FirebaseAuth
//) : FormRepository {
//    override suspend fun addForm(form: Form): Result<String> {
//        return try {
//            val currentUser =
//                auth.currentUser ?: return Result.failure(Exception("User not logged in"))
//            val formWithUser = form.copy(userId = currentUser.uid)
//            val docRef = firestore.collection("forms").add(formWithUser).await()
//            Result.success(docRef.id)
//        } catch (e: Exception) {
//            Result.failure(e)
//        }
//    }
//}
//

