package com.example.amihives

import androidx.compose.runtime.mutableStateListOf
import com.google.firebase.firestore.FirebaseFirestore
import android.R.attr.category

object EventStorage {

    val events = mutableStateListOf<Event>()
    private val db = FirebaseFirestore.getInstance()


    fun listenToEvents() {
        db.collection("events")
            .addSnapshotListener { snapshot, _ ->
                if (snapshot != null) {

                    events.clear()

                    for (doc in snapshot.documents) {
                        events.add(
                            Event(
                                id = doc.id,
                                title = doc.getString("title") ?: "",
                                description = doc.getString("description") ?: "",
                                date = doc.getString("date") ?: "",
                                venue = doc.getString("venue") ?: "",
                                imageUrl = doc.getString("imageUrl") ?: ""
                            )
                        )
                    }
                }
            }
    }

    //  ADD EVENT
    fun addEvent(
        title: String,
        desc: String,
        date: String,
        venue: String,
        imageUrl: String,
        categoty: String

    ) {
        val data = hashMapOf(
            "title" to title,
            "description" to desc,
            "date" to date,
            "venue" to venue,
            "imageUrl" to imageUrl,
            "category" to category
        )

        FirebaseFirestore.getInstance()
            .collection("events")
            .add(data)
    }

    // ️ UPDATE EVENT
    fun updateEvent(
        id: String,
        title: String,
        desc: String,
        date: String,
        venue: String,
        imageUrl: String,
        category: String
    ) {
        val data = hashMapOf(
            "title" to title,
            "description" to desc,
            "date" to date,
            "venue" to venue,
            "imageUrl" to imageUrl,
            "category" to category
        )

        FirebaseFirestore.getInstance()
            .collection("events")
            .document(id)
            .update(data as Map<String, Any>)
    }

    fun deleteEvent(id: String) {
        db.collection("events").document(id).delete()
    }
}
