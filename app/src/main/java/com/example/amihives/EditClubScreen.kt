package com.example.amihives



import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun EditClubScreen(
    club: Club,
    onBack: () -> Unit
) {

    val db = FirebaseFirestore.getInstance()

    var name by remember { mutableStateOf(club.name) }
    var category by remember { mutableStateOf(club.category) }
    var description by remember { mutableStateOf(club.description) }
    var history by remember { mutableStateOf(club.history) }
    var achievements by remember { mutableStateOf(club.achievements) }

    Column(
        modifier = Modifier
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {

        Text("Edit Club", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(name, { name = it }, label = { Text("Name") })
        OutlinedTextField(category, { category = it }, label = { Text("Category") })
        OutlinedTextField(description, { description = it }, label = { Text("Description") })
        OutlinedTextField(history, { history = it }, label = { Text("History") })
        OutlinedTextField(achievements, { achievements = it }, label = { Text("Achievements") })

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {

                val updated = mapOf(
                    "name" to name,
                    "category" to category,
                    "description" to description,
                    "history" to history,
                    "achievements" to achievements
                )

                db.collection("clubs")
                    .document(club.id)
                    .update(updated)

                onBack()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Update Club")
        }
    }
}