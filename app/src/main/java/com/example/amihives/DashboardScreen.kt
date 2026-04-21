package com.example.amihives

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import coil.compose.AsyncImage

@Composable
fun DashboardScreen(
    onEventClick: (String) -> Unit,
    onProfileClick: () -> Unit,
    onCreateEventClick: () -> Unit,
    onCreateClubClick: () -> Unit,
    onExploreClubsClick: () -> Unit
) {

    val events = EventStorage.events
    val role = UserSession.getUserRole()

    var searchQuery by remember { mutableStateOf("") }

    val filteredEvents = events.filter {
        it.title.contains(searchQuery, ignoreCase = true)
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
                .padding(16.dp)
        ) {

            //  LOWER HEADER (THIS FIXES YOUR ISSUE)
            Spacer(modifier = Modifier.height(20.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                Text(
                    "Dashboard",
                    color = Color.White,
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    "Profile",
                    color = Color(0xFF22D3EE),
                    modifier = Modifier.clickable { onProfileClick() }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // SHOW ONLY FOR USER
            if (role != "admin") {
                Button(
                    onClick = onExploreClubsClick,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF7C3AED)
                    )
                ) {
                    Text("Explore Clubs")
                }

                Spacer(modifier = Modifier.height(10.dp))
            }

            //  ADMIN CONTROLS (UNCHANGED)
            if (role == "admin") {

                Button(
                    onClick = onCreateEventClick,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF22D3EE)
                    )
                ) {
                    Text("Create Event")
                }

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = onCreateClubClick,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF38BDF8)
                    )
                ) {
                    Text("Create Club")
                }

                Spacer(modifier = Modifier.height(10.dp))
            }

            // SEARCH
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = {
                    Text("Search events...", color = Color.Gray)
                },
                modifier = Modifier.fillMaxWidth(),
                textStyle = LocalTextStyle.current.copy(color = Color.White),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    cursorColor = Color.White,
                    focusedBorderColor = Color(0xFF22D3EE),
                    unfocusedBorderColor = Color.Gray
                )
            )

            Spacer(modifier = Modifier.height(16.dp))


            //  LIST
            LazyColumn {

                items(filteredEvents) { event ->

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                            .clickable { onEventClick(event.id) } // (added back safely)
                            .border(
                                1.dp,
                                Color.White.copy(0.2f),
                                RoundedCornerShape(16.dp)
                            ),
                        colors = CardDefaults.cardColors(
                            containerColor = Color(0xFF1E293B)
                        ),
                        shape = RoundedCornerShape(16.dp)
                    ) {

                        Box {

                            Column {

                                if (event.imageUrl.isNotEmpty()) {
                                    AsyncImage(
                                        model = event.imageUrl,
                                        contentDescription = null,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(180.dp),
                                        contentScale = ContentScale.Crop
                                    )
                                }

                                Column(modifier = Modifier.padding(16.dp)) {

                                    Text(
                                        event.title,
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold
                                    )

                                    Text("  ${event.date}", color = Color.LightGray)
                                    Text("  ${event.venue}", color = Color.LightGray)
                                }
                            }

                            if (role == "admin") {

                                Text(
                                    text = "✏️",
                                    modifier = Modifier
                                        .align(Alignment.TopStart)
                                        .padding(10.dp)
                                )

                                Text(
                                    text = "❌",
                                    modifier = Modifier
                                        .align(Alignment.TopEnd)
                                        .padding(10.dp)
                                        .clickable {
                                            EventStorage.deleteEvent(event.id)
                                        }
                                )
                            }
                        }
                    }
                }
            }

        }
    }
}