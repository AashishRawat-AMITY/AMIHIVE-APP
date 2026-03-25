package com.example.amihives

import android.util.Log
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
fun LoginScreen(
    onLoginClick: () -> Unit,
    onSignupClick: () -> Unit
) {

    val auth = remember { FirebaseAuth.getInstance() }
    val db = remember { FirebaseFirestore.getInstance() }

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

        Text("Login", fontSize = 28.sp)

        Spacer(modifier = Modifier.height(20.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth(),
            enabled = !isLoading
        )

        Spacer(modifier = Modifier.height(12.dp))

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

                if (email.isBlank() || password.isBlank()) {
                    message = "Fill all fields"
                    return@Button
                }

                isLoading = true
                message = ""

                auth.signInWithEmailAndPassword(email, password)
                    .addOnSuccessListener {

                        val uid = auth.currentUser?.uid

                        if (uid != null) {

                            Log.d("LOGIN", "Auth success, UID: $uid")

                            db.collection("users")
                                .document(uid)
                                .get()
                                .addOnSuccessListener { document ->

                                    if (document.exists()) {
                                        val role = document.getString("role") ?: "user"
                                        UserSession.role = role
                                        Log.d("LOGIN", "Role: $role")
                                    } else {
                                        UserSession.role = "user"
                                        Log.d("LOGIN", "No Firestore document found")
                                    }

                                    isLoading = false
                                    onLoginClick()
                                }
                                .addOnFailureListener { e ->

                                    Log.e("LOGIN", "Firestore error", e)

                                    UserSession.role = "user"
                                    isLoading = false
                                    message = "Firestore error: ${e.message}"

                                    // Prevent freeze
                                    onLoginClick()
                                }

                        } else {
                            isLoading = false
                            message = "User error ❌"
                        }

                    }
                    .addOnFailureListener { e ->
                        isLoading = false
                        message = e.message ?: "Login Failed ❌"
                        Log.e("LOGIN", "Auth error", e)
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
                Text("Login")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (message.isNotEmpty()) {
            Text(
                text = message,
                color = Color.Red
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        TextButton(onClick = onSignupClick) {
            Text("Don't have an account? Sign Up")
        }
    }
}