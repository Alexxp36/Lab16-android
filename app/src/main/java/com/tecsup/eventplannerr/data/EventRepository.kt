package com.tecsup.eventplannerr.data

import com.tecsup.eventplannerr.model.Event
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.coroutines.tasks.await

class EventRepository(private val db: FirebaseFirestore) {

    private val eventsCollection = db.collection("events")

    fun listenUserEvents(uid: String, onChange: (List<Event>) -> Unit): ListenerRegistration {
        return eventsCollection
            .whereEqualTo("ownerUid", uid)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    // optionally handle error
                    onChange(emptyList())
                    return@addSnapshotListener
                }
                val list = snapshot?.documents?.mapNotNull { doc ->
                    doc.toObject(Event::class.java)?.apply { id = doc.id }
                } ?: emptyList()
                onChange(list)
            }
    }

    suspend fun createEvent(event: Event): Result<String> {
        return try {
            val docRef = eventsCollection.add(mapOf(
                "title" to event.title,
                "description" to event.description,
                "dateTimestamp" to event.dateTimestamp,
                "ownerUid" to event.ownerUid
            )).await()
            Result.success(docRef.id)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateEvent(event: Event): Result<Unit> {
        return try {
            val map = mapOf(
                "title" to event.title,
                "description" to event.description,
                "dateTimestamp" to event.dateTimestamp
            )
            eventsCollection.document(event.id).update(map).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deleteEvent(eventId: String): Result<Unit> {
        return try {
            eventsCollection.document(eventId).delete().await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
