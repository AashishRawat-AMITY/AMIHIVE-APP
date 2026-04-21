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
fun AllRegistrationsScreen() {

    val db = FirebaseFirestore.getInstance()
    var users by remember { mutableStateOf(listOf<Map<String, String>>()) }

    LaunchedEffect(Unit) {
        db.collection("registrations")
            .get()
            .addOnSuccessListener { result ->

                val list = mutableListOf<Map<String, String>>()

                for (doc in result) {
                    list.add(
                        mapOf(
                            "event" to (doc.getString("eventTitle") ?: "Unknown Event"),
                            "name" to (doc.getString("name") ?: "No Name"),
                            "email" to (doc.getString("email") ?: "No Email"),
                            "enrollment" to (doc.getString("enrollment") ?: "N/A"),
                            "course" to (doc.getString("course") ?: "N/A")
                        )
                    )
                }

                users = list
            }
    }


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

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
                .padding(top = 60.dp)
        ) {


            Text(
                text = "All Registrations",
                color = Color(0xFFFFD700),
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(modifier = Modifier.height(20.dp))

            LazyColumn {

                items(users) { user ->

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 14.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color(0xFF1E293B)
                        ),
                        shape = RoundedCornerShape(20.dp)
                    ) {

                        Column(modifier = Modifier.padding(16.dp)) {

                            Text(
                                text = user["event"] ?: "",
                                color = Color(0xFFFFD700),
                                style = MaterialTheme.typography.titleMedium
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            Text(
                                text = user["name"] ?: "No Name",
                                color = Color.White
                            )

                            Text(
                                text = user["email"] ?: "No Email",
                                color = Color.Gray
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = "Enrollment: ${user["enrollment"]}",
                                color = Color.White
                            )

                            Text(
                                text = "Course: ${user["course"]}",
                                color = Color.White
                            )
                        }
                    }
                }
            }
        }
    }
}