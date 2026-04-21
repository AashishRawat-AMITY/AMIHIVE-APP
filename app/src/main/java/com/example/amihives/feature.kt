package com.example.amihives

import androidx.compose.foundation.border
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.google.firebase.firestore.FirebaseFirestore
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState

@Composable
fun FeatureScreen(event: Event) {

    var showDialog by remember { mutableStateOf(false) }
    var enrollment by remember { mutableStateOf("") }
    var course by remember { mutableStateOf("") }
    var playerName by remember { mutableStateOf("") }

    val db = FirebaseFirestore.getInstance()
    val userId = UserSession.getUserId()

    var isRegistered by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(true) }


    LaunchedEffect(event.id, userId) {

        if (userId == null) return@LaunchedEffect

        db.collection("registrations")
            .document("${event.id}_$userId")
            .get()
            .addOnSuccessListener {
                isRegistered = it.exists()
                isLoading = false
            }
    }

    Column(
        modifier = Modifier
            .fillMaxSize().verticalScroll(rememberScrollState())
            .background(
                Brush.verticalGradient(
                    listOf(
                        Color(0xFF0F172A),
                        Color(0xFF1E293B),
                        Color(0xFF334155)
                    )
                )
            )
    ) {

        // IMAGE
        AsyncImage(
            model = event.imageUrl,
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp)
                .padding(10.dp)
                .border(
                    width = 3.dp,
                    color = Color(0xFFFFD700),
                    shape = RoundedCornerShape(12.dp)
                )
        )

        Card(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B))
        ) {

            Column(modifier = Modifier.padding(16.dp)) {

                Text(
                    text = event.title,
                    style = MaterialTheme.typography.headlineMedium,
                    color = Color.White
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text(" ${event.date}", color = Color.LightGray)
                Text(" ${event.venue}", color = Color.LightGray)

                Spacer(modifier = Modifier.height(12.dp))

                Text(event.description, color = Color.White)

                Spacer(modifier = Modifier.height(30.dp))

                if (isLoading) {
                    CircularProgressIndicator()
                } else {


                    Button(
                        onClick = {
                            if (userId == null) return@Button
                            showDialog = true
                        },
                        enabled = !isRegistered,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(55.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isRegistered)
                                Color.Gray
                            else
                                Color(0xFF7C3AED)
                        )
                    ) {
                        Text(
                            if (isRegistered) "Registered " else "Register",
                            color = Color.White
                        )
                    }
                }

                // DIALOG  HERE
                if (showDialog) {

                    AlertDialog(
                        onDismissRequest = { showDialog = false },

                        confirmButton = {
                            Button(onClick = {

                                if (userId == null) return@Button

                                val user = com.google.firebase.auth.FirebaseAuth.getInstance().currentUser

                                val data = hashMapOf(
                                    "eventId" to event.id,
                                    "eventTitle" to event.title,
                                    "userId" to userId,
                                    "name" to playerName,
                                    "email" to (user?.email ?: ""),
                                    "enrollment" to enrollment,
                                    "course" to course
                                )

                                db.collection("registrations")
                                    .document("${event.id}_$userId")
                                    .set(data)
                                    .addOnSuccessListener {
                                        isRegistered = true
                                        showDialog = false
                                    }

                            }) {
                                Text("Submit")
                                if (playerName.isEmpty() || enrollment.isEmpty() || course.isEmpty()) {
                                    return@Button
                                }
                            }
                        },

                        dismissButton = {
                            TextButton(onClick = { showDialog = false }) {
                                Text("Cancel")
                            }
                        },

                        title = { Text("Register") },

                        text = {
                            Column {
                                OutlinedTextField(
                                    value = playerName,
                                    onValueChange = { playerName = it },
                                    label = { Text("Player Name / Nickname ") }
                                )

                                Spacer(modifier = Modifier.height(10.dp))

                                OutlinedTextField(
                                    value = enrollment,
                                    onValueChange = { enrollment = it },
                                    label = { Text("Enrollment Number") }
                                )

                                Spacer(modifier = Modifier.height(10.dp))

                                OutlinedTextField(
                                    value = course,
                                    onValueChange = { course = it },
                                    label = { Text("Course") }
                                )
                            }
                        }
                    )
                }
            }
        }
    }
}