package com.example.amihives

import com.google.firebase.auth.FirebaseAuth

object UserSession {

    private val auth = FirebaseAuth.getInstance()

    // ✅ Login check
    fun isLoggedIn(): Boolean {
        return auth.currentUser != null
    }

    // ✅ User ID
    fun getUserId(): String? {
        return auth.currentUser?.uid
    }

    // ✅ Logout
    fun logout() {
        auth.signOut()
        role = "user"
    }

    // 🔥 ROLE SYSTEM
    var role: String = "user"

    fun isAdmin(): Boolean {
        return role == "admin"
    }
}