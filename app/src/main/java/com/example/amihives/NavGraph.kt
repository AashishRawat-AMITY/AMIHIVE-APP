package com.example.amihives

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument

@Composable
fun AppNavigation() {

    val navController = rememberNavController()

    // 🔥 SESSION CHECK
    val startDestination =
        if (UserSession.isLoggedIn()) "start" else "login"

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {

        // 🔐 LOGIN
        composable("login") {
            LoginScreen(
                onLoginClick = {
                    navController.navigate("start") {
                        popUpTo("login") { inclusive = true }
                    }
                },
                onSignupClick = {
                    navController.navigate("signup")
                }
            )
        }

        // 📝 SIGNUP
        composable("signup") {
            SignupScreen(
                onSignupDone = {
                    navController.navigate("signup_success") {
                        popUpTo("signup") { inclusive = true }
                    }
                },
                onBackToLogin = {
                    navController.popBackStack()
                }
            )
        }

        // 🎉 SIGNUP SUCCESS SCREEN
        composable("signup_success") {
            SignupSuccessScreen(
                onLoginClick = {
                    navController.navigate("login") {
                        popUpTo("signup_success") { inclusive = true }
                    }
                }
            )
        }

        // 🚀 START SCREEN
        composable("start") {
            StartScreen(
                onExploreClick = {
                    navController.navigate("dashboard")
                }
            )
        }

        // 📊 DASHBOARD
        composable("dashboard") {
            DashboardScreen(
                onEventClick = { index ->
                    navController.navigate("feature/$index")
                },
                onProfileClick = {
                    navController.navigate("profile")
                },
                onCreateEventClick = {
                    navController.navigate("create_event")
                },
                onEditEventClick = { index ->
                    navController.navigate("create_event?index=$index")
                }
            )
        }

        // ➕ CREATE / EDIT EVENT
        composable(
            route = "create_event?index={index}",
            arguments = listOf(
                navArgument("index") {
                    nullable = true
                }
            )
        ) { backStackEntry ->

            val index =
                backStackEntry.arguments?.getString("index")?.toIntOrNull()

            CreateEventScreen(
                eventIndex = index,
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }

        // 📄 FEATURE SCREEN
        composable(
            route = "feature/{index}",
            arguments = listOf(
                navArgument("index") {
                    type = NavType.IntType
                }
            )
        ) { backStackEntry ->

            val index =
                backStackEntry.arguments?.getInt("index") ?: 0

            val event = EventStorage.events.getOrNull(index)

            if (event != null) {
                FeatureScreen(event)
            }
        }

        // 👤 PROFILE
        composable("profile") {
            ProfileScreen(
                onBackClick = {
                    navController.popBackStack()
                },
                onLogoutClick = {

                    // 🔥 IMPORTANT
                    UserSession.logout()

                    navController.navigate("login") {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }
    }
}