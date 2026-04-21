package com.example.amihives

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.foundation.layout.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

@Composable
fun UploadCertificateScreen(
    eventId: String,
    userId: String
) {

    var fileUri by remember { mutableStateOf<Uri?>(null) }
    var isUploading by remember { mutableStateOf(false) }

    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri -> fileUri = uri }

    Column(modifier = Modifier.padding(16.dp)) {

        Button(onClick = { launcher.launch("*/*") }) {
            Text("Select Certificate")
        }

        Spacer(modifier = Modifier.height(10.dp))

        Button(onClick = {

            val uri = fileUri ?: return@Button
            isUploading = true

            val ref = FirebaseStorage.getInstance()
                .reference
                .child("certificates/${System.currentTimeMillis()}")

            ref.putFile(uri)
                .addOnSuccessListener {
                    ref.downloadUrl.addOnSuccessListener { url ->

                        val data = hashMapOf(
                            "eventId" to eventId,
                            "userId" to userId,
                            "certificateUrl" to url.toString(),
                            "eventTitle" to "Event"
                        )

                        FirebaseFirestore.getInstance()
                            .collection("certificates")
                            .add(data)

                        isUploading = false
                    }
                }

        }) {
            Text("Upload Certificate")
        }

        if (isUploading) {
            CircularProgressIndicator()
        }
    }
}
