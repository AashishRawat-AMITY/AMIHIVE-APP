package com.example.amihives


import androidx.compose.runtime.*
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun EditClubWrapper(
    clubId: String,
    onBack: () -> Unit
) {

    val db = FirebaseFirestore.getInstance()
    var club by remember { mutableStateOf<Club?>(null) }

    LaunchedEffect(clubId) {
        db.collection("clubs")
            .document(clubId)
            .get()
            .addOnSuccessListener {
                club = it.toObject(Club::class.java)?.apply {
                    id = it.id
                }
            }
    }

    club?.let {
        EditClubScreen(
            club = it,
            onBack = onBack
        )
    }
}