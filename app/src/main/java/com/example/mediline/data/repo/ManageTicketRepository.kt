package com.example.mediline.data.repo

import android.util.Log
import com.example.mediline.data.model.Form
import com.example.mediline.data.model.FormEntity
import com.example.mediline.data.model.PaymentStatus
import com.example.mediline.data.model.TicketStatus
import com.example.mediline.data.model.toDomain
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

interface AdminTicketRepository {

    // Fetch all tickets (maybe with filters)
    suspend fun getAllTickets(): Result<List<Form>>

    // Update ticket status (Active, Skipped, Cancelled, Closed)
    suspend fun updateTicketStatus(docId: String, newStatus: TicketStatus): Result<Unit>

    // Update payment status (Paid, Unpaid, Failed)
    suspend fun updatePaymentStatus(ticketId: String, status: PaymentStatus): Result<Unit>

    // Assign/Update department fees (if linked to ticket)
   // suspend fun updateDepartmentFee(departmentId: String, fee: Double): Result<Unit>
}


class AdminTicketRepositoryImpl(
    private val db: FirebaseFirestore
) : AdminTicketRepository {

    private val ticketCollection = db.collection("forms")

    override suspend fun getAllTickets(): Result<List<Form>> {

        return try {

            val snapshot = ticketCollection
//                .whereGreaterThanOrEqualTo("timestamp", startOfDay)
//                .whereLessThan("timestamp", endOfDay)
                .get()
                .await()

            val tickets = snapshot.documents.mapNotNull { it.toObject(FormEntity::class.java)?.toDomain() }
            Result.success(tickets)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    override suspend fun updateTicketStatus(docId: String, newStatus: TicketStatus): Result<Unit> {
        return try {
            ticketCollection
                .document(docId)
                .update("ticketStatus", newStatus.name)
                .await()
            Log.d("AdminTicketRepository", "Ticket status updated successfully")
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }


//    override suspend fun updateTicketStatus(ticketId: String, status: TicketStatus): Result<Unit> {
//        return try {
//            ticketCollection.document(ticketId).update("ticketStatus", status.name).await()
//            Result.success(Unit)
//        } catch (e: Exception) {
//            Result.failure(e)
//        }
//    }

    override suspend fun updatePaymentStatus(ticketId: String, status: PaymentStatus): Result<Unit> {
        return try {
            ticketCollection.document(ticketId).update("paymentStatus", status.name).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }


}

