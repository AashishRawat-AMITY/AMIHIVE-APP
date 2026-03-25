package com.example.amihives

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*

@Composable
fun DashboardScreen(
    onEventClick: (Int) -> Unit,
    onProfileClick: () -> Unit,
    onCreateEventClick: () -> Unit,
    onEditEventClick: (Int) -> Unit
) {

    var searchQuery by remember { mutableStateOf("") }

    val events = EventStorage.events

    // 🔥 LOAD EVENTS FROM FIREBASE
    LaunchedEffect(Unit) {
        EventStorage.listenToEvents()
    }

    val filteredEvents = events.filter {
        it.title.contains(searchQuery, ignoreCase = true)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF0F2F5))
    ) {

        // 🔷 HEADER
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
                .background(Color(0xFF6200EE))
        ) {

            Text(
                text = "College Club Updates",
                color = Color.White,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.Center)
            )

            // 👤 PROFILE ICON
            IconButton(
                onClick = onProfileClick,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(top = 28.dp, end = 12.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Profile",
                    tint = Color.White
                )
            }
        }

        // 🔍 SEARCH
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = { Text("Search events...") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )

        // ➕ CREATE EVENT (ADMIN ONLY)
        if (UserSession.isAdmin()) {
            Button(
                onClick = onCreateEventClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                Text("Create Event")
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // 📋 EVENT LIST
        LazyColumn(
            contentPadding = PaddingValues(16.dp)
        ) {

            items(filteredEvents) { event ->

                EventCard(
                    event = event,
                    isAdmin = UserSession.isAdmin(),

                    onClick = {
                        val index = EventStorage.events.indexOf(event)
                        onEventClick(index)
                    },

                    onEditClick = {
                        val index = EventStorage.events.indexOf(event)
                        onEditEventClick(index)
                    },

                    onDeleteClick = {
                        EventStorage.deleteEvent(event)
                    }
                )
            }
        }
    }
}

@Composable
fun EventCard(
    event: Event,
    isAdmin: Boolean,
    onClick: () -> Unit,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit
) {

    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF6200EE))
    ) {

        Column(
            modifier = Modifier.padding(16.dp)
        ) {

            Text(
                text = event.title,
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Date: ${event.date}",
                color = Color.White.copy(alpha = 0.8f)
            )

            Text(
                text = "Venue: ${event.venue}",
                color = Color.White.copy(alpha = 0.8f)
            )

            // 🔥 ADMIN CONTROLS
            if (isAdmin) {

                Spacer(modifier = Modifier.height(8.dp))

                Row {

                    TextButton(onClick = onEditClick) {
                        Text("Edit", color = Color.White)
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    TextButton(onClick = onDeleteClick) {
                        Text("Delete", color = Color.White)
                    }
                }
            }
        }
    }
}