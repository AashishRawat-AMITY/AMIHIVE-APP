//package com.example.amihives
//import android.widget.Toast
//import androidx.compose.ui.platform.LocalContext
//import coil.compose.AsyncImage
//import androidx.compose.foundation.rememberScrollState
//import androidx.compose.foundation.verticalScroll
//import android.net.Uri
//import androidx.activity.compose.rememberLauncherForActivityResult
//import androidx.activity.result.contract.ActivityResultContracts
//import androidx.compose.foundation.background
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material3.*
//import androidx.compose.material3.ExposedDropdownMenuBox
//import androidx.compose.material3.ExposedDropdownMenuDefaults
//import androidx.compose.material3.DropdownMenuItem
//import androidx.compose.runtime.*
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.graphics.Brush
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.Alignment
//import com.google.firebase.firestore.FirebaseFirestore
//import com.google.firebase.storage.FirebaseStorage
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun CreateClubScreen(onBackClick: () -> Unit) {
//
//    var name by remember { mutableStateOf("") }
//    var selectedCategory by remember { mutableStateOf("") }
//    var description by remember { mutableStateOf("") }
//    var imageUri by remember { mutableStateOf<Uri?>(null) }
//
//    var expanded by remember { mutableStateOf(false) }
//    var isLoading by remember { mutableStateOf(false) }
//
//    val categories = listOf(
//        "Coding",
//        "Competitive Coding",
//        "Music",
//        "Sports",
//        "Art",
//        "Dance"
//    )
//
//    val launcher = rememberLauncherForActivityResult(
//        ActivityResultContracts.GetContent()
//    ) { uri ->
//        println("🔥 SELECTED IMAGE URI: $uri")
//        imageUri = uri
//    }
//
//    Box(
//        modifier = Modifier
//            .fillMaxSize()
//            .background(
//                Brush.verticalGradient(
//                    listOf(
//                        Color(0xFF020617),
//                        Color(0xFF0F172A),
//                        Color(0xFF1E3A8A)
//                    )
//                )
//            )
//    ) {
//
//        val scrollState = rememberScrollState()
//
//        Column(
//            modifier = Modifier
//                .fillMaxSize()
//                .verticalScroll(scrollState).padding(16.dp)
//        ){
//
//
//
//            // 🔥 GOLDEN HEADING
//            Text(
//                text = "ABOUT CLUB !",
//                color = Color(0xFFFFD700),
//                fontSize = MaterialTheme.typography.headlineMedium.fontSize,
//                fontWeight = FontWeight.Bold
//            )
//
//            Spacer(modifier = Modifier.height(30.dp))
//
//
//
//            // 🔥 CLUB NAME
//            OutlinedTextField(
//                value = name,
//                onValueChange = { name = it },
//                label = { Text("Club Name", color = Color.White) },
//                textStyle = LocalTextStyle.current.copy(color = Color.White),
//                colors = TextFieldDefaults.colors(
//                    focusedTextColor = Color.White,
//                    unfocusedTextColor = Color.White,
//                    focusedContainerColor = Color(0xFF1E293B),
//                    unfocusedContainerColor = Color(0xFF1E293B),
//                    focusedIndicatorColor = Color.Cyan,
//                    unfocusedIndicatorColor = Color.Gray
//                ),
//                modifier = Modifier.fillMaxWidth()
//            )
//
//            Spacer(modifier = Modifier.height(16.dp))
//
//            // 🔥 WORKING DROPDOWN
//            ExposedDropdownMenuBox(
//                expanded = expanded,
//                onExpandedChange = { expanded = !expanded }
//            ) {
//
//                OutlinedTextField(
//                    value = selectedCategory,
//                    onValueChange = {},
//                    readOnly = true,
//                    label = { Text("Select Category", color = Color.White) },
//                    trailingIcon = {
//                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
//                    },
//                    textStyle = LocalTextStyle.current.copy(color = Color.White),
//                    colors = TextFieldDefaults.colors(
//                        focusedTextColor = Color.White,
//                        unfocusedTextColor = Color.White,
//                        focusedContainerColor = Color(0xFF1E293B),
//                        unfocusedContainerColor = Color(0xFF1E293B)
//                    ),
//                    modifier = Modifier
//                        .menuAnchor()
//                        .fillMaxWidth()
//                )
//
//                ExposedDropdownMenu(
//                    expanded = expanded,
//                    onDismissRequest = { expanded = false }
//                ) {
//                    categories.forEach { category ->
//                        DropdownMenuItem(
//                            text = { Text(category) },
//                            onClick = {
//                                selectedCategory = category
//                                expanded = false
//                            }
//                        )
//                    }
//                }
//            }
//
//            Spacer(modifier = Modifier.height(16.dp))
//
//            // 🔥 DESCRIPTION
//            OutlinedTextField(
//                value = description,
//                onValueChange = { description = it },
//                label = { Text("Description", color = Color.White) },
//                textStyle = LocalTextStyle.current.copy(color = Color.White),
//                colors = TextFieldDefaults.colors(
//                    focusedTextColor = Color.White,
//                    unfocusedTextColor = Color.White,
//                    focusedContainerColor = Color(0xFF1E293B),
//                    unfocusedContainerColor = Color(0xFF1E293B)
//                ),
//                modifier = Modifier.fillMaxWidth()
//            )
//
//            Spacer(modifier = Modifier.height(16.dp))
//
//            // 🔥 IMAGE BUTTON
//            Button(
//                onClick = { launcher.launch("image/*") },
//                modifier = Modifier.fillMaxWidth()
//            ) {
//                Text("Select Image")
//            }
//
//
//            imageUri?.let {
//                Spacer(modifier = Modifier.height(10.dp))
//
//                AsyncImage(
//                    model = it,
//                    contentDescription = null,
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .height(150.dp)
//                )
//            }
//
//            Spacer(modifier = Modifier.height(20.dp))
//
//
//
//            // 🔥 CREATE BUTTON
//            val context = LocalContext.current
//
//            Button(
//                onClick = {
//
//                    if (name.isBlank() || selectedCategory.isBlank()) {
//                        Toast.makeText(context, "Fill all fields", Toast.LENGTH_SHORT).show()
//                        return@Button
//                    }
//
//                    val uri = imageUri
//                    if (uri == null) {
//                        Toast.makeText(context, "Select an image", Toast.LENGTH_SHORT).show()
//                        return@Button
//                    }
//
//                    isLoading = true
//
//                    val ref = FirebaseStorage.getInstance()
//                        .reference
//                        .child("club_images/${System.currentTimeMillis()}")
//
//                    ref.putFile(uri)
//                        .addOnSuccessListener {
//
//                            ref.downloadUrl.addOnSuccessListener { url ->
//
//                                val club = hashMapOf(
//                                    "name" to name,
//                                    "category" to selectedCategory,
//                                    "description" to description,
//                                    "imageUrl" to url.toString()
//                                )
//
//                                FirebaseFirestore.getInstance()
//                                    .collection("clubs")
//                                    .add(club)
//                                    .addOnSuccessListener {
//
//                                        isLoading = false
//                                        Toast.makeText(
//                                            context,
//                                            "Club Created ✅",
//                                            Toast.LENGTH_SHORT
//                                        ).show()
//                                        onBackClick()
//                                    }
//                                    .addOnFailureListener {
//                                        isLoading = false
//                                        Toast.makeText(
//                                            context,
//                                            "Firestore Failed ❌",
//                                            Toast.LENGTH_SHORT
//                                        ).show()
//                                    }
//                            }
//                        }
//                        .addOnFailureListener {
//                            isLoading = false
//                            Toast.makeText(context, "Image Upload Failed ❌", Toast.LENGTH_SHORT)
//                                .show()
//                        }
//                },
//                enabled = !isLoading,
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .height(55.dp),
//                shape = RoundedCornerShape(16.dp),
//                colors = ButtonDefaults.buttonColors(
//                    containerColor = Color(0xFF7C3AED)
//                )
//            ) {
//                Text("Create Club", color = Color.White)
//            }
//
//            if (isLoading) {
//                Spacer(modifier = Modifier.height(16.dp))
//                CircularProgressIndicator(color = Color.White)
//            }
//        }
//    }
//}
//

package com.example.amihives

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.platform.LocalContext
import coil.compose.AsyncImage
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateClubScreen(onBackClick: () -> Unit) {

    var name by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var imageUri by remember { mutableStateOf<Uri?>(null) }

    var expanded by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }

    val categories = listOf(
        "Coding",
        "Competitive Coding",
        "Music",
        "Sports",
        "Art",
        "Dance"
    )

    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri ->
        imageUri = uri
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
                .padding(top = 80.dp, bottom = 16.dp) //  ONLY CHANGE
        ) {

            //  HEADING
            Text(
                text = "ABOUT CLUB !",
                color = Color(0xFFFFD700),
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(30.dp))

            //  CLUB NAME
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Club Name", color = Color.White) },
                textStyle = LocalTextStyle.current.copy(color = Color.White),
                colors = TextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedContainerColor = Color(0xFF1E293B),
                    unfocusedContainerColor = Color(0xFF1E293B),
                    focusedIndicatorColor = Color(0xFFFFD700),
                    unfocusedIndicatorColor = Color.Gray
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 🔥 CATEGORY DROPDOWN
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
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                    },
                    textStyle = LocalTextStyle.current.copy(color = Color.White),
                    colors = TextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedContainerColor = Color(0xFF1E293B),
                        unfocusedContainerColor = Color(0xFF1E293B),
                        focusedIndicatorColor = Color(0xFFFFD700),
                        unfocusedIndicatorColor = Color.Gray
                    ),
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth()
                )

                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    categories.forEach { category ->
                        DropdownMenuItem(
                            text = { Text(category) },
                            onClick = {
                                selectedCategory = category
                                expanded = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            //  DESCRIPTION
            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Description", color = Color.White) },
                textStyle = LocalTextStyle.current.copy(color = Color.White),
                colors = TextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedContainerColor = Color(0xFF1E293B),
                    unfocusedContainerColor = Color(0xFF1E293B),
                    focusedIndicatorColor = Color(0xFFFFD700),
                    unfocusedIndicatorColor = Color.Gray
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // IMAGE BUTTON
            Button(
                onClick = { launcher.launch("image/*") },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Select Image")
            }

            imageUri?.let {
                Spacer(modifier = Modifier.height(10.dp))
                AsyncImage(
                    model = it,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            val context = LocalContext.current

            //  CREATE BUTTON
            Button(
                onClick = {

                    if (name.isBlank() || selectedCategory.isBlank()) {
                        Toast.makeText(context, "Fill all fields", Toast.LENGTH_SHORT).show()
                        return@Button
                    }

                    val uri = imageUri
                    if (uri == null) {
                        Toast.makeText(context, "Select an image", Toast.LENGTH_SHORT).show()
                        return@Button
                    }

                    isLoading = true

                    val ref = FirebaseStorage.getInstance()
                        .reference
                        .child("club_images/${System.currentTimeMillis()}")

                    ref.putFile(uri)
                        .addOnSuccessListener {

                            ref.downloadUrl.addOnSuccessListener { url ->

                                val club = hashMapOf(
                                    "name" to name,
                                    "category" to selectedCategory,
                                    "description" to description,
                                    "imageUrl" to url.toString()
                                )

                                FirebaseFirestore.getInstance()
                                    .collection("clubs")
                                    .add(club)
                                    .addOnSuccessListener {

                                        isLoading = false
                                        Toast.makeText(
                                            context,
                                            "Club Created",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        onBackClick()
                                    }
                                    .addOnFailureListener {
                                        isLoading = false
                                        Toast.makeText(
                                            context,
                                            "Firestore Failed ",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                            }
                        }
                        .addOnFailureListener {
                            isLoading = false
                            Toast.makeText(context, "Image Upload Failed ", Toast.LENGTH_SHORT)
                                .show()
                        }
                },
                enabled = !isLoading,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(55.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF7C3AED)
                )
            ) {
                Text("Create Club", color = Color.White)
            }

            if (isLoading) {
                Spacer(modifier = Modifier.height(16.dp))
                CircularProgressIndicator(color = Color.White)
            }
        }
    }
}