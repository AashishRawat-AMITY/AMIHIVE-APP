package com.example.amihives

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.firebase.firestore.FirebaseFirestore

data class RegisteredUser(
    val name: String = "",
    val email: String = ""
)

@Composable
fun EventRegistrationsScreen(eventId: String) {

    val db = FirebaseFirestore.getInstance()

    var users by remember { mutableStateOf(listOf<RegisteredUser>()) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(eventId) {

        isLoading = true

        db.collection("registrations")
            .whereEqualTo("eventId", eventId)
            .get()
            .addOnSuccessListener { result ->

                val tempList = mutableListOf<RegisteredUser>()

                if (result.isEmpty) {
                    users = emptyList()
                    isLoading = false
                    return@addOnSuccessListener
                }

                var count = result.size()

                for (doc in result) {

                    val userId = doc.getString("userId") ?: continue

                    //  FETCH USER DETAILS
                    db.collection("users")
                        .document(userId)
                        .get()
                        .addOnSuccessListener { userDoc ->

                            val name = userDoc.getString("name") ?: "Unknown"
                            val email = userDoc.getString("email") ?: "No Email"

                            tempList.add(RegisteredUser(name, email))

                            count--

                            //  when all users fetched
                            if (count == 0) {
                                users = tempList
                                isLoading = false
                            }
                        }
                }
            }
            .addOnFailureListener {
                isLoading = false
            }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
            .padding(16.dp)
    ) {

        Text(
            "Registered Students",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        when {
            isLoading -> {
                CircularProgressIndicator()
            }

            users.isEmpty() -> {
                Text("No registrations yet")
            }

            else -> {
                LazyColumn {

                    items(users) { user ->

                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 6.dp)
                        ) {

                            Column(modifier = Modifier.padding(12.dp)) {

                                Text(
                                    text = user.name,
                                    style = MaterialTheme.typography.titleMedium
                                )

                                Text(
                                    text = user.email,
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}