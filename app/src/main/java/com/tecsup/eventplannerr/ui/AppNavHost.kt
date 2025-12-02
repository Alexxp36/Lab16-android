package com.tecsup.eventplannerr.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.*
import com.tecsup.eventplannerr.data.AuthRepository
import com.tecsup.eventplannerr.data.EventRepository
import com.tecsup.eventplannerr.ui.screens.LoginScreen
import com.tecsup.eventplannerr.ui.screens.RegisterScreen
import com.tecsup.eventplannerr.ui.screens.EventsListScreen
import com.tecsup.eventplannerr.ui.screens.CreateEditEventScreen

@Composable
fun AppNavHost(navController: NavHostController = rememberNavController(), authRepo: AuthRepository, eventRepo: EventRepository) {
    val nav = navController
    NavHost(nav, startDestination = "login") {
        composable("login") {
            LoginScreen(authRepo = authRepo, onLoginSuccess = {
                nav.navigate("events") {
                    popUpTo("login") { inclusive = true }
                }
            }, onNavigateRegister = { nav.navigate("register") })
        }
        composable("register") {
            RegisterScreen(authRepo = authRepo, onRegisterSuccess = {
                nav.navigate("events") {
                    popUpTo("register") { inclusive = true }
                }
            }, onBack = { nav.popBackStack() })
        }
        composable("events") {
            EventsListScreen(authRepo = authRepo, eventRepo = eventRepo,
                onCreateEvent = { nav.navigate("create_edit_event") },
                onEditEvent = { eventId ->
                    // For simplicity navigate to create/edit route, you could pass params
                    nav.navigate("create_edit_event?eventId=$eventId")
                },
                onSignOut = {
                    nav.navigate("login") {
                        popUpTo(0)
                    }
                }
            )
        }
        composable("create_edit_event") {
            CreateEditEventScreen(authRepo = authRepo, eventRepo = eventRepo, onSaved = {
                nav.popBackStack()
            }, onCancel = { nav.popBackStack() })
        }
    }
}
