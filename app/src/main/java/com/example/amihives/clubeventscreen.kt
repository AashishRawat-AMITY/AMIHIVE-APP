package com.example.amihives
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border

import coil.compose.AsyncImage

import com.google.firebase.firestore.FirebaseFirestore
import androidx.compose.ui.graphics.Color

import com.example.amihives.Event
@Composable
fun ClubEventsScreen(clubId: String) {

    val db = FirebaseFirestore.getInstance()
    var events by remember { mutableStateOf(listOf<Event>()) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(clubId) {

        db.collection("events")
            .whereEqualTo("clubId", clubId)
            .get()
            .addOnSuccessListener { result ->

                val list = mutableListOf<Event>()

                for (doc in result) {
                    val event = doc.toObject(Event::class.java).copy(id = doc.id)
                    list.add(event)
                }

                events = list.filter { isEventUpcoming(it.date) }
                isLoading = false
            }
            .addOnFailureListener {
                isLoading = false
            }
    }



    when {


        isLoading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Color(0xFFFFD700))
            }
        }

        //  EMPTY STATE (VERY IMPORTANT FIX)
        events.isEmpty() -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No events available ",
                    color = Color.White
                )
            }
        }

        // NORMAL LIST
        else -> {
            LazyColumn(modifier = Modifier.padding(16.dp)) {

                items(events) { event ->

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        shape = RoundedCornerShape(16.dp),
                        border = BorderStroke(1.dp, Color.White),
                        colors = CardDefaults.cardColors(
                            containerColor = Color(0xFF1E293B)
                        )
                    ) {

                        Column {

                            if (event.imageUrl.isNotEmpty()) {
                                Box(
                                    modifier = Modifier
                                        .padding(10.dp)
                                        .border(
                                            BorderStroke(2.dp, Color(0xFFFFD700)),
                                            RoundedCornerShape(12.dp)
                                        )
                                ) {
                                    AsyncImage(
                                        model = event.imageUrl,
                                        contentDescription = null,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(180.dp),
                                        contentScale = ContentScale.Crop
                                    )
                                }
                            }

                            Column(modifier = Modifier.padding(12.dp)) {

                                Text(
                                    event.title,
                                    style = MaterialTheme.typography.titleMedium,
                                    color = Color(0xFFFFD700)
                                )

                                Spacer(modifier = Modifier.height(6.dp))

                                Text(" ${event.date}", color = Color.White)
                                Text("  ${event.venue}", color = Color.LightGray)
                            }
                        }
                    }
                }
            }
        }
    }
}