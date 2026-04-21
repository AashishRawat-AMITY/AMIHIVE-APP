package com.example.amihives

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun StartScreen(
    onExploreClick: () -> Unit = {}
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF020617), // top (almost black)
                        Color(0xFF0F172A), // middle (dark navy)
                        Color(0xFF1E3A8A)  // bottom (blue)
                    )
                )
            )
    ) {

        //  FULL WIDTH IMAGE (NO SIDE GAP)
        Image(
            painter = painterResource(id = R.drawable.logo1_0),
            contentDescription = "AMIHIVE Logo",
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .fillMaxWidth()     // edge-to-edge
                .align(Alignment.Center)
        )

        // BOTTOM CONTENT
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Bottom
        ) {

            // TAGLINE
            Text(
                text = "Linked: Stay in the Loop.",
                fontSize = 22.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFFE2E8F0),
                modifier = Modifier.padding(bottom = 20.dp)
            )

            // BUTTON
            Button(
                onClick = { onExploreClick() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(55.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF22D3EE) // cyan match
                )
            ) {
                Text(
                    text = "Explore",
                    fontSize = 18.sp,
                    color = Color.Black
                )
            }
        }
    }
}