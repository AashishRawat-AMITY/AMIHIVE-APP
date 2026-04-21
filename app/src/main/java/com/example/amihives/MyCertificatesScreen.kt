package com.example.amihives

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

data class Certificate(
    val eventTitle: String = "",
    val certificateUrl: String = ""
)

@Composable
fun MyCertificatesScreen() {

    val userId = FirebaseAuth.getInstance().currentUser?.uid
    val db = FirebaseFirestore.getInstance()
    val context = LocalContext.current

    var certificates by remember { mutableStateOf(listOf<Certificate>()) }

    LaunchedEffect(Unit) {

        if (userId == null) return@LaunchedEffect

        db.collection("certificates")
            .whereEqualTo("userId", userId)
            .get()
            .addOnSuccessListener { result ->

                certificates = result.map {
                    Certificate(
                        eventTitle = it.getString("eventTitle") ?: "Event",
                        certificateUrl = it.getString("certificateUrl") ?: ""
                    )
                }
            }
    }

    Column(modifier = Modifier.padding(16.dp)) {

        Text("My Certificates", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(10.dp))

        LazyColumn {

            items(certificates) { cert ->

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp)
                ) {

                    Column(modifier = Modifier.padding(12.dp)) {

                        Text(cert.eventTitle)

                        Spacer(modifier = Modifier.height(6.dp))

                        Button(onClick = {
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(cert.certificateUrl))
                            context.startActivity(intent)
                        }) {
                            Text("Download Certificate")
                        }
                    }
                }
            }
        }
    }
}