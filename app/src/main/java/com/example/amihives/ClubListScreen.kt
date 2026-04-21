package com.example.amihives

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.background
import coil.compose.AsyncImage
import com.google.firebase.firestore.FirebaseFirestore
import com.example.amihives.Club
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.border






@Composable
fun ClubListScreen(
    category: String,
    onClubClick: (Club) -> Unit,
    onEditClick: (Club) -> Unit
) {

    val db = FirebaseFirestore.getInstance()
    var clubs by remember { mutableStateOf(listOf<Club>()) }

    LaunchedEffect(category) {

        db.collection("clubs")
            .whereEqualTo("category", category)
            .get()
            .addOnSuccessListener { result ->

                val list = mutableListOf<Club>()

                for (doc in result) {
                    val club = doc.toObject(Club::class.java)
                    club.id = doc.id
                    list.add(club)
                }

                clubs = list
            }
    }

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
    ) {

        LazyColumn(
            modifier = Modifier.padding(16.dp)
        ) {


                items(clubs) { club ->

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 10.dp),
                        shape = RoundedCornerShape(24.dp), // smoother like category
                        border = BorderStroke(1.5.dp, Color.White),
                        colors = CardDefaults.cardColors(
                            containerColor = Color(0xFF1E293B)
                        )
                    ) {

                        Column {


                            Box(
                                modifier = Modifier
                                    .padding(10.dp)
                                    .border(
                                        BorderStroke(2.dp, Color(0xFFFFD700)),
                                        RoundedCornerShape(12.dp)
                                    )
                            ) {
                                if (club.imageUrl.isNotEmpty()) {
                                    AsyncImage(
                                        model = club.imageUrl,
                                        contentDescription = null,
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(180.dp)
                                    )
                                }
                            }

                            Column(modifier = Modifier.padding(12.dp)) {


                                Text(
                                    club.name,
                                    style = MaterialTheme.typography.titleMedium,
                                    color = Color(0xFFFFD700)
                                )

                                Spacer(modifier = Modifier.height(6.dp))

                                //  NOT REMOVED, JUST DIMMED
                                Text(
                                    club.description,
                                    color = Color.Gray,
                                    maxLines = 1
                                )

                                Spacer(modifier = Modifier.height(8.dp))

                                Button(onClick = {
                                    onClubClick(club)
                                }) {
                                    Text("View Details")
                                }
                            }
                        }
                    }
                }
            }
        }
    }


