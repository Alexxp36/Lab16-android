package com.tecsup.eventplannerr

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.navigation.compose.rememberNavController
import com.tecsup.eventplannerr.ui.AppNavHost
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.tecsup.eventplannerr.data.AuthRepository
import com.tecsup.eventplannerr.data.EventRepository
import com.tecsup.eventplannerr.viewmodel.AuthViewModel
import com.tecsup.eventplannerr.viewmodel.EventViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner

class MainActivity : ComponentActivity() {

    // Factory helpers omitted — we'll create ViewModel instances manually in composables for simplicity.
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val authRepo = AuthRepository(FirebaseAuth.getInstance())
        val eventRepo = EventRepository(FirebaseFirestore.getInstance())

        setContent {
            MaterialTheme {
                Surface {
                    val navController = rememberNavController()
                    AppNavHost(
                        navController = navController,
                        authRepo = authRepo,
                        eventRepo = eventRepo
                    )
                }
            }
        }
    }
}
