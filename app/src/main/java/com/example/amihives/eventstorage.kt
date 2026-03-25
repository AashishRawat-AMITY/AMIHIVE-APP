package com.example.amihives

import androidx.compose.runtime.mutableStateListOf
import com.google.firebase.firestore.FirebaseFirestore

object EventStorage {

    val events = mutableStateListOf<Event>()
    private val db = FirebaseFirestore.getInstance()

    // 🔥 REAL-TIME LISTENER
    fun listenToEvents() {
        db.collection("events")
            .addSnapshotListener { result, error ->

                if (error != null) return@addSnapshotListener

                events.clear()

                result?.forEach { doc ->
                    val event = doc.toObject(Event::class.java)
                    event.id = doc.id   // 🔥 CRITICAL FIX
                    events.add(event)
                }
            }
    }

    // 🔥 ADD
    fun addEvent(event: Event) {
        db.collection("events").add(event)
    }

    // 🔥 UPDATE (FIXED)
    fun updateEvent(event: Event) {

        if (event.id.isEmpty()) {
            println("❌ Update failed: ID empty")
            return
        }

        db.collection("events")
            .document(event.id)
            .set(event)
            .addOnSuccessListener {
                println("✅ Updated")
            }
    }

    // 🔥 DELETE
    fun deleteEvent(event: Event) {
        if (event.id.isNotEmpty()) {
            db.collection("events").document(event.id).delete()
        }
    }
}