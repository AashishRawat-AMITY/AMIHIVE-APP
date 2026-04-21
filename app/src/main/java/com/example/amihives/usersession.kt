package com.example.amihives

import android.content.Context
import android.content.SharedPreferences
import com.google.firebase.auth.FirebaseAuth

object UserSession {

    private const val PREF_NAME = "user_session"
    private const val KEY_LOGGED_IN = "is_logged_in"
    private const val KEY_ROLE = "user_role"

    private lateinit var prefs: SharedPreferences

    fun init(context: Context) {
        prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    fun login(role: String) {
        prefs.edit()
            .putBoolean(KEY_LOGGED_IN, true)
            .putString(KEY_ROLE, role)
            .apply()
    }

    fun logout() {
        FirebaseAuth.getInstance().signOut()
        prefs.edit().clear().apply()
    }

    fun isLoggedIn(): Boolean {
        return if (::prefs.isInitialized) {
            prefs.getBoolean(KEY_LOGGED_IN, false)
        } else false
    }

    fun getUserRole(): String {
        return if (::prefs.isInitialized) {
            prefs.getString(KEY_ROLE, "user") ?: "user"
        } else "user"
    }

    fun getUserId(): String? {
        return FirebaseAuth.getInstance().currentUser?.uid
    }
}