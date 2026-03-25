package com.example.amihives

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.google.firebase.storage.FirebaseStorage

@Composable
fun CreateEventScreen(
    eventIndex: Int? = null,
    onBackClick: () -> Unit
) {

    val existingEvent = eventIndex?.let { EventStorage.events[it] }

    var title by remember { mutableStateOf(existingEvent?.title ?: "") }
    var date by remember { mutableStateOf(existingEvent?.date ?: "") }
    var venue by remember { mutableStateOf(existingEvent?.venue ?: "") }
    var description by remember { mutableStateOf(existingEvent?.description ?: "") }
    var prize by remember { mutableStateOf(existingEvent?.prize ?: "") }

    var imageUri by remember {
        mutableStateOf(existingEvent?.imageUri?.let { Uri.parse(it) })
    }

    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? -> imageUri = uri }

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

            OutlinedTextField(title, { title = it }, label = { Text("Title") })
            OutlinedTextField(date, { date = it }, label = { Text("Date") })
            OutlinedTextField(venue, { venue = it }, label = { Text("Venue") })
            OutlinedTextField(description, { description = it }, label = { Text("Description") })
            OutlinedTextField(prize, { prize = it }, label = { Text("Prize") })

            Spacer(modifier = Modifier.height(10.dp))

            Button(onClick = { launcher.launch("image/*") }) {
                Text("Select Image")
            }

            imageUri?.let {
                AsyncImage(
                    model = it,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                )
            }
        }

        Button(
            onClick = {

                val storage = FirebaseStorage.getInstance()
                val ref = storage.reference.child("event_images/${System.currentTimeMillis()}")

                val uri = imageUri ?: return@Button

                ref.putFile(uri)
                    .continueWithTask { ref.downloadUrl }
                    .addOnSuccessListener { url ->

                        val event = Event(
                            id = existingEvent?.id ?: "", // 🔥 FIXED
                            title = title,
                            date = date,
                            venue = venue,
                            description = description,
                            prize = prize,
                            imageUri = url.toString()
                        )

                        if (eventIndex != null) {
                            EventStorage.updateEvent(event)
                        } else {
                            EventStorage.addEvent(event)
                        }

                        onBackClick()
                    }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(if (eventIndex != null) "Update Event" else "Post Event")
        }
    }
}