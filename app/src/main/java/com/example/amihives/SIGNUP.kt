package com.example.amihives

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import androidx.compose.foundation.background
import androidx.compose.ui.graphics.Brush


@Composable
fun SignupScreen(
    onSignupDone: () -> Unit,
    onBackToLogin: () -> Unit
) {

    val auth = remember { FirebaseAuth.getInstance() }
    val db = remember { FirebaseFirestore.getInstance() }

    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    var isLoading by remember { mutableStateOf(false) }
    var message by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background( //  DARK GRADIENT
                Brush.verticalGradient(
                    listOf(
                        Color(0xFF020617),
                        Color(0xFF0F172A),
                        Color(0xFF1E3A8A)
                    )
                )
            )
            .padding(24.dp),
        verticalArrangement = Arrangement.Center
    ) {

        Text(
            "Sign Up",
            fontSize = 28.sp,
            color = Color(0xFFFFD700) // GOLD TITLE
        )

        Spacer(modifier = Modifier.height(20.dp))

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Name") },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFFFFD700),
                unfocusedBorderColor = Color(0xFFFFD700),
                focusedLabelColor = Color(0xFFFFD700),
                cursorColor = Color(0xFFFFD700),
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White
            )
        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFFFFD700),
                unfocusedBorderColor = Color(0xFFFFD700),
                focusedLabelColor = Color(0xFFFFD700),
                cursorColor = Color(0xFFFFD700),
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White
            )
        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFFFFD700),
                unfocusedBorderColor = Color(0xFFFFD700),
                focusedLabelColor = Color(0xFFFFD700),
                cursorColor = Color(0xFFFFD700),
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White
            )
        )

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = {

                if (name.isBlank() || email.isBlank() || password.isBlank()) {
                    message = "Please fill all fields"
                    return@Button
                }

                isLoading = true
                message = ""

                auth.createUserWithEmailAndPassword(email, password)
                    .addOnSuccessListener {

                        val uid = auth.currentUser?.uid

                        if (uid != null) {

                            val user = hashMapOf(
                                "name" to name,
                                "email" to email,
                                "role" to "user"
                            )

                            db.collection("users")
                                .document(uid)
                                .set(user)
                                .addOnSuccessListener {
                                    isLoading = false
                                    onSignupDone()
                                }
                                .addOnFailureListener {
                                    isLoading = false
                                    message = "Failed to save user "
                                }
                        }
                    }
                    .addOnFailureListener {
                        isLoading = false
                        message = it.message ?: "Signup Failed "
                    }

            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF7C3AED)
            )
        ) {
            Text("Create Account")
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (message.isNotEmpty()) {
            Text(
                text = message,
                color = Color.Red
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        TextButton(onClick = onBackToLogin) {
            Text(
                "Back to Login",
                color = Color.White
            )
        }
    }
}