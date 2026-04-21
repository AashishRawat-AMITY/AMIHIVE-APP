
package com.example.amihives

import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.google.firebase.storage.FirebaseStorage
import java.util.*
import androidx.compose.foundation.clickable
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.layout.ContentScale

// 🔥 NEW IMPORTS (DATE PICKER)
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.rememberDatePickerState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateEventScreen(
    eventId: String? = null,
    onBackClick: () -> Unit
) {

    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var date by remember { mutableStateOf("") }
    var venue by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("") }

    var expanded by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }

    //  NEW STATE (DATE PICKER)
    val datePickerState = rememberDatePickerState()
    var showDatePicker by remember { mutableStateOf(false) }

    val categories = listOf(
        "Coding",
        "Competitive Coding",
        "Music",
        "Sports",
        "Art",
        "Dance"
    )

    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }

    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri ->
        selectedImageUri = uri
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

        val scrollState = rememberScrollState()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(horizontal = 16.dp)
                .padding(top = 100.dp, bottom = 40.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = if (eventId == null) "CREATE EVENT !" else "EDIT EVENT",
                color = Color(0xFFFFD700),
                fontSize = MaterialTheme.typography.headlineMedium.fontSize,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(30.dp))

            // TITLE
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Title", color = Color.White) },
                textStyle = LocalTextStyle.current.copy(color = Color.White),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color(0xFF1E293B),
                    unfocusedContainerColor = Color(0xFF1E293B),
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // DESCRIPTION
            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Description", color = Color.White) },
                textStyle = LocalTextStyle.current.copy(color = Color.White),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color(0xFF1E293B),
                    unfocusedContainerColor = Color(0xFF1E293B)
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            //  UPDATED DATE FIELD (ONLY CHANGE)
            val interactionSource = remember { MutableInteractionSource() }

            OutlinedTextField(
                value = date,
                onValueChange = {},
                readOnly = true,
                label = { Text("Select Date", color = Color.White) },
                textStyle = LocalTextStyle.current.copy(color = Color.White),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color(0xFF1E293B),
                    unfocusedContainerColor = Color(0xFF1E293B)
                ),
                modifier = Modifier.fillMaxWidth(),
                interactionSource = interactionSource
            )

//  LISTEN FOR CLICK
            LaunchedEffect(interactionSource) {
                interactionSource.interactions.collect {
                    showDatePicker = true
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // VENUE
            OutlinedTextField(
                value = venue,
                onValueChange = { venue = it },
                label = { Text("Venue", color = Color.White) },
                textStyle = LocalTextStyle.current.copy(color = Color.White),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color(0xFF1E293B),
                    unfocusedContainerColor = Color(0xFF1E293B)
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // CATEGORY
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded }
            ) {

                OutlinedTextField(
                    value = selectedCategory,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Select Category", color = Color.White) },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded)
                    },
                    textStyle = LocalTextStyle.current.copy(color = Color.White),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color(0xFF1E293B),
                        unfocusedContainerColor = Color(0xFF1E293B)
                    ),
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth()
                )

                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    categories.forEach {
                        DropdownMenuItem(
                            text = { Text(it) },
                            onClick = {
                                selectedCategory = it
                                expanded = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = { launcher.launch("image/*") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(55.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF6366F1)
                )
            ) {
                Text("Select Image", color = Color.White)
            }

            selectedImageUri?.let {
                Spacer(modifier = Modifier.height(10.dp))

                AsyncImage(
                    model = it,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp),
                    contentScale = ContentScale.Crop
                )
            }

            Spacer(modifier = Modifier.height(25.dp))

            Button(
                onClick = {

                    val uri = selectedImageUri ?: return@Button

                    isLoading = true

                    val ref = FirebaseStorage.getInstance()
                        .reference
                        .child("event_images/${System.currentTimeMillis()}")

                    ref.putFile(uri)
                        .addOnSuccessListener {

                            ref.downloadUrl.addOnSuccessListener { url ->

                                EventStorage.addEvent(
                                    title,
                                    description,
                                    date,
                                    venue,
                                    url.toString(),
                                    selectedCategory
                                )

                                isLoading = false
                                onBackClick()
                            }
                        }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(55.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF7C3AED)
                )
            ) {
                Text("Create Event", color = Color.White)
            }

            if (isLoading) {
                Spacer(modifier = Modifier.height(16.dp))
                CircularProgressIndicator(color = Color.White)
            }
        }

        //  DATE PICKER DIALOG (NEW)
        if (showDatePicker) {
            DatePickerDialog(
                onDismissRequest = { showDatePicker = false },
                confirmButton = {
                    TextButton(
                        onClick = {
                            val selectedDateMillis = datePickerState.selectedDateMillis

                            if (selectedDateMillis != null) {
                                val sdf = java.text.SimpleDateFormat("dd/MM/yyyy", java.util.Locale.getDefault())
                                date = sdf.format(java.util.Date(selectedDateMillis))
                            }

                            showDatePicker = false
                        }
                    ) {
                        Text("OK")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDatePicker = false }) {
                        Text("Cancel")
                    }
                }
            ) {
                DatePicker(state = datePickerState)
            }
        }
    }
}