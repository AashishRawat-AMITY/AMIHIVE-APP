package com.example.amihives

data class Event(
    var id: String = "",   // 🔥 MUST BE VAR
    var title: String = "",
    var date: String = "",
    var venue: String = "",
    var description: String = "",
    var prize: String = "",
    var imageUri: String = ""
)