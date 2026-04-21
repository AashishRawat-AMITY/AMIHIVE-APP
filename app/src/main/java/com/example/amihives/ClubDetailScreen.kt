package com.example.amihives

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import coil.compose.AsyncImage
import androidx.compose.foundation.border
import androidx.compose.foundation.BorderStroke



@Composable
fun ClubDetailScreen(
    club: Club,
    onViewEventsClick: (String) -> Unit
) {

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
            .verticalScroll(rememberScrollState())
    ) {


        Box(
            modifier = Modifier
                .padding(12.dp)
                .border(
                    BorderStroke(2.dp, Color(0xFFFFD700)),
                    RoundedCornerShape(16.dp)
                )
        ) {
            AsyncImage(
                model = club.imageUrl,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(260.dp),
                contentScale = ContentScale.Crop
            )
        }

        Column(
            modifier = Modifier
                .padding(16.dp)
                .background(
                    Color(0xFF1E293B),
                    shape = RoundedCornerShape(16.dp)
                )
                .padding(16.dp)
        ) {


            Text(
                club.name,
                style = MaterialTheme.typography.headlineMedium,
                color = Color(0xFFFFD700)
            )

            Spacer(modifier = Modifier.height(10.dp))


            Text(
                text = club.description,
                color = Color.LightGray
            )

            Spacer(modifier = Modifier.height(20.dp))



            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = {
                    onViewEventsClick(club.id)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(55.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF7C3AED)
                )
            ) {
                Text("View Events ", color = Color.White)
            }
        }
    }
}