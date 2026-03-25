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
                        Color.White,
                        Color(0xFFEDE7F6)
                    )
                )
            )
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {

            Spacer(modifier = Modifier.height(10.dp))

            // 🔥 BIG LOGO
            Image(
                painter = painterResource(id = R.drawable.applogo2),
                contentDescription = "App Logo",
                modifier = Modifier
                    .fillMaxHeight(0.5f)
                    .fillMaxWidth(),
                contentScale = ContentScale.Fit
            )

            // 🔥 TAGLINE
            Text(
                text = "Linked: Stay in the Loop.",
                fontSize = 25.sp,
                fontWeight = FontWeight.Medium,
                color = Color.DarkGray,
                modifier = Modifier.padding(vertical = 10.dp)
            )

            // 🔥 EXPLORE BUTTON
            Button(
                onClick = { onExploreClick() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(55.dp),
                shape = RoundedCornerShape(14.dp)
            ) {
                Text(
                    text = "Explore",
                    fontSize = 18.sp
                )
            }
        }
    }
}