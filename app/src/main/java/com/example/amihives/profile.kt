package com.example.amihives

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.example.amihives.isEventUpcoming

@Composable
fun ProfileScreen(
    onBackClick: () -> Unit = {},
    onLogoutClick: () -> Unit = {},
    onCertificatesClick: (String) -> Unit,

    onViewRegistrationsClick: () -> Unit
) {

    val auth = FirebaseAuth.getInstance()
    val db = FirebaseFirestore.getInstance()

    var name by remember { mutableStateOf("Loading...") }
    var email by remember { mutableStateOf("Loading...") }
    var role by remember { mutableStateOf("user") }


    val isAdmin = role == "admin"

    val registeredEvents = remember { mutableStateListOf<String>() }

    LaunchedEffect(Unit) {

        val uid = auth.currentUser?.uid

        if (uid != null) {

            //  FETCH USER
            db.collection("users")
                .document(uid)
                .get()
                .addOnSuccessListener {
                    name = it.getString("name") ?: "No Name"
                    email = it.getString("email") ?: "No Email"
                    role = it.getString("role") ?: "user"
                }

            //  FETCH REGISTRATIONS
            db.collection("registrations")
                .whereEqualTo("userId", uid)
                .get()
                .addOnSuccessListener { result ->

                    registeredEvents.clear()

                    result.forEach { reg ->

                        val eventId = reg.getString("eventId")

                        if (eventId != null) {

                            // FETCH EVENT DATA FROM EVENTS COLLECTION
                            db.collection("events")
                                .document(eventId)
                                .get()
                                .addOnSuccessListener { eventDoc ->

                                    val title = eventDoc.getString("title")
                                    val date = eventDoc.getString("date")

                                    //  FILTER APPLIED HERE
                                    if (title != null && date != null && isEventUpcoming(date)) {
                                        registeredEvents.add(title)
                                    }
                                }
                        }
                    }
                }
        }
    }

    //  BACKGROUND
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
                .systemBarsPadding()
                .verticalScroll(rememberScrollState())
        ) {

            //  TOP BAR
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBackClick) {
                    Icon(Icons.Default.ArrowBack, null, tint = Color.White)
                }

                Text(
                    "Profile",
                    color = Color.White,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            //  PROFILE
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Box(
                    modifier = Modifier
                        .size(110.dp)
                        .shadow(20.dp, CircleShape)
                        .background(
                            Brush.linearGradient(
                                listOf(Color(0xFF7C3AED), Color(0xFF22D3EE))
                            ),
                            CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Default.Person,
                        null,
                        tint = Color.White,
                        modifier = Modifier.size(50.dp)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(name, fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color.White)
                Text(email, fontSize = 14.sp, color = Color.LightGray)
            }

            Spacer(modifier = Modifier.height(20.dp))

            //  STATS
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B)),
                shape = RoundedCornerShape(20.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    ProfileStat("Events", registeredEvents.size.toString())
                    ProfileStat("Clubs", "2")
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            //  TITLE
            Text(
                "My Events",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 16.dp)
            )

            Spacer(modifier = Modifier.height(10.dp))

            //  EVENTS LIST
            LazyColumn(
                modifier = Modifier
                    .heightIn(max = 300.dp)
                    .padding(horizontal = 16.dp)
            ) {
                items(registeredEvents) { event ->
                    EventItem(event)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))



            //  CERTIFICATE BUTTON (DYNAMIC)
            Button(
                onClick = {
                    onCertificatesClick(role)   // PASS ROLE HERE
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .height(55.dp),
                shape = RoundedCornerShape(18.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF7C3AED) // Purple
                )
            ) {
                Text(
                    text = if (isAdmin) "certPOST" else "certVIEW ",
                    fontSize = 16.sp,
                    color = Color.White
                )
            }
            Spacer(modifier = Modifier.height(20.dp))

            if (isAdmin) {
                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        onViewRegistrationsClick()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .height(55.dp),
                    shape = RoundedCornerShape(18.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFADD8E6)
                    )
                ) {
                    Text("View Registrations ", color = Color.Black)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // LOGOUT BUTTON
            Button(
                onClick = onLogoutClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .height(55.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFDC2626) // Softer red
                )
            ) {
                Text("Logout", color = Color.White)
            }

            Spacer(modifier = Modifier.height(30.dp))
        }
    }
}

@Composable
fun ProfileStat(title: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(value, fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color.White)
        Text(title, fontSize = 13.sp, color = Color.Gray)
    }
}

@Composable
fun EventItem(name: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .shadow(8.dp, RoundedCornerShape(16.dp)),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B)),
        shape = RoundedCornerShape(16.dp)
    ) {
        Text(
            text = name,
            modifier = Modifier.padding(16.dp),
            fontSize = 16.sp,
            color = Color.White
        )
    }
}