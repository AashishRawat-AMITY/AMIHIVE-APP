package com.example.amihives

import java.text.SimpleDateFormat
import java.util.*

fun isEventUpcoming(dateString: String): Boolean {
    return try {
        val format = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        format.isLenient = false

        val eventDate = format.parse(dateString)
        val today = Date()

        eventDate != null && !eventDate.before(today)
    } catch (e: Exception) {
        false
    }
}

