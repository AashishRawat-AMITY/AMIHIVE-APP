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
import android.widget.Toast
import androidx.compose.ui.platform.LocalContext

import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun CertPostScreen() {

    var email by remember { mutableStateOf("") }
    var certUrl by remember { mutableStateOf("") }
    var eventName by remember { mutableStateOf("") }

    val db = FirebaseFirestore.getInstance()
    val context = LocalContext.current

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
                "POST CERTIFICATE",
                color = Color(0xFFFFD700),
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(30.dp))

            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {

                    // EMAIL
                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("User Email") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = TextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            focusedLabelColor = Color(0xFFFFD700),
                            unfocusedLabelColor = Color.Gray
                        )
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // EVENT NAME
                    OutlinedTextField(
                        value = eventName,
                        onValueChange = { eventName = it },
                        label = { Text("Event Name") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = TextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            focusedLabelColor = Color(0xFFFFD700),
                            unfocusedLabelColor = Color.Gray
                        )
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // CERT URL
                    OutlinedTextField(
                        value = certUrl,
                        onValueChange = { certUrl = it },
                        label = { Text("Certificate URL") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = TextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            focusedLabelColor = Color(0xFFFFD700),
                            unfocusedLabelColor = Color.Gray
                        )
                    )

                    Spacer(modifier = Modifier.height(30.dp))

                    // BUTTON
                    Button(
                        onClick = {

                            // VALIDATION
                            if (email.isEmpty() || certUrl.isEmpty() || eventName.isEmpty()) {
                                Toast.makeText(context, "Fill all fields", Toast.LENGTH_SHORT).show()
                                return@Button
                            }

                            val data = hashMapOf(
                                "certUrl" to certUrl,
                                "eventName" to eventName
                            )


                            db.collection("certificates")
                                .document(email.trim())
                                .collection("userCertificates")
                                .add(data)
                                .addOnSuccessListener {
                                    Toast.makeText(
                                        context,
                                        "Certificate Uploaded ",
                                        Toast.LENGTH_SHORT
                                    ).show()

                                    // clear fields (optional)
                                    certUrl = ""
                                    eventName = ""
                                }
                                .addOnFailureListener {
                                    Toast.makeText(
                                        context,
                                        "Upload Failed ",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFFFD700)
                        )
                    ) {
                        Text(
                            "Upload Certificate ",
                            color = Color.Black,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}