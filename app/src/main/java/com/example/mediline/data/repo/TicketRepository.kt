package com.example.mediline.data.repo

import android.util.Log
import com.example.mediline.data.model.Form
import com.example.mediline.data.model.TicketRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import com.example.mediline.data.model.FormEntity
import com.example.mediline.data.model.TicketStatus
import com.example.mediline.data.model.toDomain
import com.google.firebase.firestore.toObjects

class TicketRepositoryImpl(
    private val db: FirebaseFirestore,
    private val auth: FirebaseAuth
) : TicketRepository {

    override suspend fun getTickets(): Result<List<Form>> = try {
        val userId = auth.currentUser?.uid

        val snapshot = db.collection("forms")
            .whereEqualTo("userId", userId) // ✅ use current user instead of hardcoded
            .get()
            .await()

        val entities = snapshot.toObjects(FormEntity::class.java)
        val tickets = entities.map { it.toDomain() }.sortedBy { it.timeStamp }

        Result.success(tickets)
    } catch (e: Exception) {
        Log.e("TicketRepo", "Error fetching tickets", e)
        Result.failure(e)
    }


}







