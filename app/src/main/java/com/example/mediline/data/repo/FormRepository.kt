package com.example.mediline.data.repo

import android.util.Log
import com.example.mediline.User.ui.authentication.LoginScreen
import com.example.mediline.data.model.AdminFormRepository
import com.example.mediline.data.model.CreatorRole
import com.example.mediline.data.model.Form
import com.example.mediline.data.model.FormRepository
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
            var result=""

            // Run Firestore transaction
            val ticketNo = firestore.runTransaction { transaction ->
                val snapshot = transaction.get(deptRef)
                val currentCounter = snapshot.getLong("todayCounter") ?: 0
                val newCounter = currentCounter + 1

                // Update today's counter
                transaction.update(deptRef, "todayCounter", newCounter)
                val formDocRef = firestore.collection("forms").document() // generates ID

             result=formDocRef.id
                // Create form with ticketNumber and userId
                val formWithTicket = form.copy(
                    id = formDocRef.id,
                    createdBy = currentUser.uid,
                    userId = currentUser.uid,
                    ticketNumber = newCounter,
                    timeStamp = System.currentTimeMillis()
                )

                transaction.set(formDocRef, formWithTicket.copy(id = formDocRef.id))
                newCounter
            }.await()
            // Converts Task<Long> to suspend-friendly value

            Result.success(result)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updatePaymentStatus(formId: String, status: String): Result<Unit> {
        return try {
            firestore.collection("forms")
                .document(formId)
                .update("paymentStatus", status)
                .await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

}


class AdminFormRepositoryImpl(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
) : AdminFormRepository {

    override suspend fun addTicketForUser(form: Form, userId: String?): Result<String> {
        return try {

            val currentUser = auth.currentUser ?: return Result.failure(Exception("User not logged in"))
            val deptRef = firestore.collection("departments")
                .document(form.departmentId)

            val ticketNo = firestore.runTransaction { transaction ->
                val snapshot = transaction.get(deptRef)
                val currentCounter = snapshot.getLong("todayCounter") ?: 0
                val newCounter = currentCounter + 1
                transaction.update(deptRef, "todayCounter", newCounter)

                val formDocRef = firestore.collection("forms").document()
                val formWithTicket = form.copy(
                    id = formDocRef.id,
                    createdBy = currentUser.uid,
                    creatorRole = CreatorRole.ADMIN,// or admin UID if you want
                    userId = "",     // can be null if patient not registered
                    ticketNumber = newCounter,
                    timeStamp = System.currentTimeMillis()
                )
                transaction.set(formDocRef, formWithTicket)
                newCounter
            }.await()
            Log.d("AdminFormRepositoryImpl", "Ticket created! No: $ticketNo")
            Result.success("Ticket created! No: $ticketNo")
        } catch (e: Exception) {
            Log.e("AdminFormRepositoryImpl", "Error adding ticket", e)
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

