package com.example.amihives

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun HomeScreen(
    onEventClick: (Event) -> Unit
) {

    val events = EventStorage.events

    // 🔥 LOAD EVENTS FROM FIREBASE (REAL-TIME)
    LaunchedEffect(Unit) {
        EventStorage.listenToEvents()
    }

    Scaffold(
        topBar = {
            // ✅ SIMPLE TOP BAR (NO VERSION ISSUE)
            Text(
                text = "Events",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )
        }
    ) { padding ->

        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {

            items(events) { event ->

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp)
                        .clickable { onEventClick(event) }
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {

                        Text(
                            text = event.title,
                            style = MaterialTheme.typography.titleLarge
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text("📅 ${event.date}")
                        Text("📍 ${event.venue}")
                    }
                }
            }
        }
    }
}