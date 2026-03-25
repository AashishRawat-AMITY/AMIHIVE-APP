package com.example.amihives

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun ProfileScreen(
    onBackClick: () -> Unit = {},
    onLogoutClick: () -> Unit = {}
) {

    val auth = FirebaseAuth.getInstance()
    val db = FirebaseFirestore.getInstance()

    var name by remember { mutableStateOf("Loading...") }
    var email by remember { mutableStateOf("Loading...") }

    val registeredEvents = remember { mutableStateListOf<String>() }

    // 🔥 FETCH USER + REGISTERED EVENTS
    LaunchedEffect(Unit) {
        val uid = auth.currentUser?.uid

        if (uid != null) {

            // 🔹 USER DATA
            db.collection("users")
                .document(uid)
                .get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        name = document.getString("name") ?: "No Name"
                        email = document.getString("email") ?: "No Email"
                    }
                }

            // 🔹 REGISTERED EVENTS
            db.collection("registrations")
                .whereEqualTo("userId", uid)
                .get()
                .addOnSuccessListener { result ->
                    registeredEvents.clear()
                    result.forEach {
                        val title = it.getString("eventTitle")
                        title?.let { registeredEvents.add(it) }
                    }
                }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF0F2F5))
    ) {

        // 🔝 TOP BAR
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF6200EE))
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            IconButton(onClick = onBackClick) {
                Icon(Icons.Default.ArrowBack, contentDescription = null, tint = Color.White)
            }

            Text(
                text = "Profile",
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // 👤 PROFILE
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Box(
                modifier = Modifier
                    .size(90.dp)
                    .background(Color(0xFF6200EE), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.Person, contentDescription = null, tint = Color.White)
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(name, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Text(email, fontSize = 14.sp, color = Color.Gray)
        }

        Spacer(modifier = Modifier.height(24.dp))

        // 📊 STATS
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                ProfileStat("Events Joined", registeredEvents.size.toString())
                ProfileStat("Clubs", "2")
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // 🎫 MY EVENTS
        Text(
            text = "My Events",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(start = 16.dp)
        )

        Spacer(modifier = Modifier.height(10.dp))

        Column(modifier = Modifier.padding(horizontal = 16.dp)) {

            if (registeredEvents.isEmpty()) {
                Text("No events joined yet", color = Color.Gray)
            } else {
                registeredEvents.forEach {
                    EventItem(it)
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        // 🔴 LOGOUT
        Button(
            onClick = {
                FirebaseAuth.getInstance().signOut()
                onLogoutClick()
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
        ) {
            Text("Logout")
        }
    }
}

@Composable
fun ProfileStat(title: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(value, fontSize = 20.sp, fontWeight = FontWeight.Bold)
        Text(title, fontSize = 14.sp, color = Color.Gray)
    }
}

@Composable
fun EventItem(name: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Text(
            text = name,
            modifier = Modifier.padding(16.dp),
            fontSize = 16.sp
        )
    }
}