package com.example.amihives

import androidx.compose.runtime.*
import androidx.navigation.NavHostController
import com.google.firebase.firestore.FirebaseFirestore
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.Box
import androidx.compose.ui.Alignment
import androidx.compose.material3.Text
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.ui.graphics.Color

@Composable
fun ClubDetailWrapper(
    clubId: String,
    navController: NavHostController
) {

    val db = FirebaseFirestore.getInstance()
    var club by remember { mutableStateOf<Club?>(null) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(clubId) {
        db.collection("clubs")
            .document(clubId)
            .get()
            .addOnSuccessListener {
                club = it.toObject(Club::class.java)?.apply {
                    id = it.id
                }
                isLoading = false
            }
            .addOnFailureListener {
                isLoading = false
            }
    }

    // 🔥 UI HANDLING

    when {
        isLoading -> {

            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Color(0xFFFFD700))
            }
        }

        club != null -> {

            ClubDetailScreen(
                club = club!!,
                onViewEventsClick = { id ->
                    navController.navigate("club_events/$id")
                }
            )
        }

        else -> {

            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "Failed to load club ",
                    color = Color.White
                )
            }
        }
    }
}