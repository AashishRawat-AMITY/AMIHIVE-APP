

package com.example.amihives

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun RegisteredUsersScreen(eventId: String) {

    val db = FirebaseFirestore.getInstance()

    var users by remember { mutableStateOf(listOf<Map<String, String>>()) }

    LaunchedEffect(Unit) {
        db.collection("registrations")
            .whereEqualTo("eventId", eventId)   // FILTER BY EVENT
            .get()
            .addOnSuccessListener { result ->

                val list = mutableListOf<Map<String, String>>()

                for (doc in result) {
                    list.add(
                        mapOf(
                            "name" to (doc.getString("name") ?: "No Name"),
                            "email" to (doc.getString("email") ?: ""),
                            "enrollment" to (doc.getString("enrollment") ?: ""),
                            "course" to (doc.getString("course") ?: "")
                        )
                    )
                }

                users = list
            }
    }

    //  UI SAME STYLE AS YOUR APP
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(
                        Color(0xFF020617),
                        Color(0xFF0F172A),
                        Color(0xFF1E3A8A)
                    )
                )
            )
    ) {

        Column(modifier = Modifier.padding(16.dp)) {

            Text(
                "Registered Users",
                color = Color(0xFFFFD700),
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(modifier = Modifier.height(20.dp))

            LazyColumn {

                items(users) { user ->

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 12.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B)),
                        shape = RoundedCornerShape(16.dp)
                    ) {

                        Column(modifier = Modifier.padding(16.dp)) {

                            Text(user["name"] ?: "", color = Color.White)
                            Text(user["email"] ?: "", color = Color.Gray)

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                "Enrollment: ${user["enrollment"]}",
                                color = Color.White
                            )

                            Text(
                                "Course: ${user["course"]}",
                                color = Color.White
                            )
                        }
                    }
                }
            }
        }
    }
}