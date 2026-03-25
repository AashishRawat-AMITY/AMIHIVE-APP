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
            .padding(24.dp),
        verticalArrangement = Arrangement.Center
    ) {

        Text("Sign Up", fontSize = 28.sp)

        Spacer(modifier = Modifier.height(20.dp))

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Name") },
            modifier = Modifier.fillMaxWidth(),
            enabled = !isLoading
        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth(),
            enabled = !isLoading
        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            modifier = Modifier.fillMaxWidth(),
            enabled = !isLoading
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
                                "role" to "user"   // 🔥 default role
                            )

                            db.collection("users")
                                .document(uid)
                                .set(user)
                                .addOnSuccessListener {

                                    isLoading = false
                                    message = "Account Created 🎉"
                                    onSignupDone()
                                }
                                .addOnFailureListener {
                                    isLoading = false
                                    message = "Failed to save user ❌"
                                }

                        } else {
                            isLoading = false
                            message = "User error ❌"
                        }
                    }
                    .addOnFailureListener {
                        isLoading = false
                        message = it.message ?: "Signup Failed ❌"
                    }

            },
            modifier = Modifier.fillMaxWidth(),
            enabled = !isLoading
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    strokeWidth = 2.dp
                )
            } else {
                Text("Create Account")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (message.isNotEmpty()) {
            Text(
                text = message,
                color = if (message.contains("🎉")) Color(0xFF4CAF50) else Color.Red
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        TextButton(onClick = onBackToLogin) {
            Text("Back to Login")
        }
    }
}