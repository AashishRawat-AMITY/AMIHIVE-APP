package com.example.amihives

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun FeatureScreen(event: Event) {

    val isAdmin = UserSession.isAdmin()
    val db = FirebaseFirestore.getInstance()
    val auth = FirebaseAuth.getInstance()

    var isRegistered by remember { mutableStateOf(false) }
    val userId = auth.currentUser?.uid

    // 🔥 CHECK REGISTRATION
    LaunchedEffect(Unit) {
        if (userId != null) {
            db.collection("registrations")
                .whereEqualTo("userId", userId)
                .whereEqualTo("eventId", event.id)
                .get()
                .addOnSuccessListener {
                    isRegistered = !it.isEmpty
                }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
        ) {

            AsyncImage(
                model = event.imageUri,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            )

            Text(event.title, style = MaterialTheme.typography.headlineMedium)

            Text("📅 ${event.date}")
            Text("📍 ${event.venue}")
            Text("🏆 ${event.prize}")

            Text("Description:")
            Text(event.description)
        }

        if (!isAdmin) {

            if (isRegistered) {
                Button(
                    onClick = {},
                    enabled = false,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Registered ✅")
                }
            } else {
                Button(
                    onClick = {

                        if (userId == null) return@Button

                        val data = hashMapOf(
                            "userId" to userId,
                            "eventId" to event.id,
                            "eventTitle" to event.title
                        )

                        db.collection("registrations")
                            .add(data)
                            .addOnSuccessListener {
                                isRegistered = true
                            }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Register")
                }
            }
        }
    }
}