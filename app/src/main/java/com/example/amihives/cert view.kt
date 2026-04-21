package com.example.amihives
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import android.content.Intent
import android.net.Uri
import androidx.compose.ui.platform.LocalContext
import androidx.compose.foundation.clickable

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun CertViewScreen() {

    var certList by remember { mutableStateOf(listOf<Pair<String, String>>()) }

    val user = FirebaseAuth.getInstance().currentUser
    val email = user?.email ?: ""

    LaunchedEffect(Unit) {
        val db = FirebaseFirestore.getInstance()

        db.collection("certificates")
            .document(email)
            .collection("userCertificates")
            .get()
            .addOnSuccessListener { result ->

                val tempList = mutableListOf<Pair<String, String>>()

                for (doc in result) {
                    val event = doc.getString("eventName") ?: ""
                    val url = doc.getString("certUrl") ?: ""

                    tempList.add(Pair(event, url))
                }

                certList = tempList
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
                .padding(horizontal = 20.dp)
                .padding(top = 80.dp)
        ) {

            Text(
                "MY CERTIFICATE ",
                color = Color(0xFFFFD700),
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(30.dp))

            val context = LocalContext.current

            if (certList.isNotEmpty()) {

                certList.forEach { cert ->

                    Card(
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B)),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 12.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {

                            Text(
                                text = cert.first, // event name
                                color = Color(0xFFFFD700),
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            Text(
                                "Certificate Link:",
                                color = Color.White
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            Text(
                                text = cert.second,
                                color = Color(0xFF22D3EE),
                                modifier = Modifier.clickable {
                                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(cert.second))
                                    context.startActivity(intent)
                                }
                            )
                        }
                    }
                }

            } else {

                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {

                        Text(
                            "No Certificate Found ",
                            color = Color(0xFFFFD700)
                        )
                    }
                }
            }
        }
    }
}