package com.example.mediline.data.repo

import android.util.Log
import com.example.mediline.data.model.Form
import com.example.mediline.data.model.TicketRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import com.example.mediline.data.model.FormEntity
import com.example.mediline.data.model.toDomain
import com.google.firebase.firestore.toObjects

class TicketRepositoryImpl(
    private val db: FirebaseFirestore,
    private val auth: FirebaseAuth
) : TicketRepository {

    override suspend fun getTickets(): Result<List<Form>> = try {
        val userId= auth.currentUser?.uid

        val snapshot = db.collection("forms")
            .whereEqualTo("userId",
                "tLwAmbqR7cWKQekSjBc19woTif83")

            .get()
            .await()

        Log.d("TicketRepo","$snapshot")

        val entities = snapshot.toObjects(FormEntity::class.java)

//        val validEntities = entities.filter {
//            try {
//                // This line tries to get an enum value for the payment status.
//                // If it fails, the entry is filtered out.
//                PaymentStatus.valueOf(it.paymentStatus.uppercase())
//                true // Keep this entry
//            } catch (e: IllegalArgumentException) {
//                // The status string does not match an enum value
//                // Log the issue and filter out the entry
//                Log.w("TicketRepo", "Invalid paymentStatus found: ${it.paymentStatus}")
//                false // Filter this entry out
//            }
//        }


      val tickets = entities.map { it.toDomain() }

        Result.success(tickets)
    } catch (e: Exception) {
        Log.e("TicketRepo", "Error fetching tickets", e)  // ✅ log error with stacktrace

        Result.failure(e)
    }
}


//  val tickets = snapshot.toObjects(FormEntity::class.java).map { it.toDomain() }

//        val tickets = snapshot.documents.mapNotNull { doc ->
//            doc.toObject(Form::class.java)?.let { form ->
//                Form(
//                    name = form.name,
//                    address = form.address,
//                    phone = form.phone,
//                    age = form.age,
//                    sex = form.sex,
//                    timeStamp = form.timeStamp,
//                    ticketNumber = form.ticketNumber,
//                    ticketStatus = form.ticketStatus,
//                    departmentId =form.departmentId ,
//                    userId = form.userId,
//                    opdNo = form.opdNo,
//                    paymentStatus = form.paymentStatus
//                )
//            }
//        }
