package com.example.amihives

import androidx.compose.runtime.*
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import kotlinx.coroutines.delay
import com.google.firebase.auth.FirebaseAuth

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    AppNavGraph(navController)
}

@Composable
fun AppNavGraph(navController: NavHostController) {

    //   CHECK LOGIN STATE
    val startDestination = if (UserSession.isLoggedIn()) {
        "splash"
    } else {
        "login"
    }

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {


        composable("categories") {
            CategoryScreen(
                onCategoryClick = { category ->
                    navController.navigate("clubs/$category")
                }
            )
        }


        composable("clubs/{category}") { backStackEntry ->
            val category = backStackEntry.arguments?.getString("category") ?: ""

            ClubListScreen(
                category = category,
                onClubClick = { club ->
                    navController.navigate("club_detail/${club.id}")
                },
                onEditClick = { } // keep as is
            )
        }

        //  LOGIN
        composable("login") {
            LoginScreen(
                onLoginClick = {
                    navController.navigate("splash") {
                        popUpTo("login") { inclusive = true }
                    }
                },
                onSignupClick = {
                    navController.navigate("signup")
                }
            )
        }

        //  SIGNUP
        composable("signup") {
            SignupScreen(
                onSignupDone = {
                    navController.navigate("splash") {
                        popUpTo("signup") { inclusive = true }
                    }
                },
                onBackToLogin = {
                    navController.popBackStack()
                }
            )
        }
        composable("create_club") {
            CreateClubScreen(
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
        //  SPLASH
        composable("splash") {

            SplashScreen()

            LaunchedEffect(Unit) {
                delay(2000)

                navController.navigate("start") {
                    popUpTo("splash") { inclusive = true }
                }
            }
        }

        //  START SCREEN
        composable("start") {
            StartScreen(
                onExploreClick = {
                    navController.navigate("dashboard") {
                        popUpTo("start") { inclusive = true }
                    }
                }
            )
        }


        composable("club_detail/{clubId}") { backStack ->
            val clubId = backStack.arguments?.getString("clubId") ?: ""
            ClubDetailWrapper(clubId, navController)
        }

        composable("club_events/{clubId}") { backStack ->
            val clubId = backStack.arguments?.getString("clubId") ?: ""
            ClubEventsScreen(clubId)
        }

        // DASHBOARD
        composable("dashboard") {
            DashboardScreen(
                onEventClick = { id ->
                    navController.navigate("feature/$id")
                },
                onProfileClick = {
                    navController.navigate("profile")
                },
                onCreateEventClick = {
                    navController.navigate("create_event")
                },
                onCreateClubClick = {
                    navController.navigate("create_club")
                },
                onExploreClubsClick = {
                    navController.navigate("categories")
                }
            )
        }

        // EVENT DETAIL
        composable("feature/{eventId}") { backStackEntry ->
            val eventId = backStackEntry.arguments?.getString("eventId")
            val event = EventStorage.events.find { it.id == eventId }

            event?.let {
                FeatureScreen(event = it)
            }
        }

        //  CREATE EVENT
        composable("create_event") {
            CreateEventScreen(
                onBackClick = { navController.popBackStack() }
            )
        }

        // this is for registered user
        composable("registered_users/{eventId}") { backStackEntry ->
            val eventId = backStackEntry.arguments?.getString("eventId") ?: ""
            RegisteredUsersScreen(eventId)
        }

        //  this is all registration in admin profile
        composable("all_registrations") {
            AllRegistrationsScreen()
        }
        //  PROFILE
        composable("profile") {
            ProfileScreen(

                onBackClick = {
                    navController.popBackStack()
                },

                onLogoutClick = {
                    UserSession.logout()
                    navController.navigate("login") {
                        popUpTo("dashboard") { inclusive = true }
                    }
                },

                onCertificatesClick = { role ->
                    if (role == "admin") {
                        navController.navigate("cert_post")
                    } else {
                        navController.navigate("cert_view")
                    }
                },onViewRegistrationsClick = {
                    navController.navigate("all_registrations")
                }

            )
        }
        composable(route = "cert_post") {
            CertPostScreen()
        }

        composable(route = "cert_view") {
            CertViewScreen()
        }
    }
}