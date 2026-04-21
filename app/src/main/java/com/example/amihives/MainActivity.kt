package com.example.amihives

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        UserSession.init(applicationContext)


        EventStorage.listenToEvents()

        setContent {
            AppNavigation()
        }
    }
}