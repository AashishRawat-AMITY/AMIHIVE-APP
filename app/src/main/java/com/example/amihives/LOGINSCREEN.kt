package com.example.amihives

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import androidx.compose.foundation.background
import androidx.compose.ui.graphics.Brush



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
            .background(
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
            "Login",
            fontSize = 28.sp,
            color = Color(0xFFFFD700)
        )

        Spacer(modifier = Modifier.height(20.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth(),
            enabled = !isLoading,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFFFFD700),
                unfocusedBorderColor = Color(0xFFFFD700),
                focusedLabelColor = Color(0xFFFFD700),
                cursorColor = Color(0xFFFFD700),
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White
            )
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            enabled = !isLoading,
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

                                    val role = document.getString("role") ?: "user"

                                    UserSession.login(role)

                                    Log.d("LOGIN", "Role: $role")

                                    isLoading = false
                                    onLoginClick()
                                }
                                .addOnFailureListener { e ->

                                    isLoading = false
                                    message = "Failed to load user role"

                                    Log.e("LOGIN", "Firestore error", e)
                                }

                        } else {
                            isLoading = false
                            message = "User not found "
                        }
                    }
                    .addOnFailureListener { e ->
                        isLoading = false
                        message = e.message ?: "Login Failed "
                        Log.e("LOGIN", "Auth error", e)
                    }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = !isLoading,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF7C3AED)
            )
        ) {

            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    strokeWidth = 2.dp,
                    color = Color.White
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
            Text(
                "Don't have an account? Sign Up",
                color = Color.White
            )
        }
    }
}