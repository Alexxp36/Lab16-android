package com.tecsup.eventplannerr

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.tecsup.eventplannerr.ui.theme.EventPlannerrTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Probar conexión Firebase
        try {
            val auth = Firebase.auth
            val firestore = Firebase.firestore

            Log.d("Firebase", "✅ Firebase Auth inicializado correctamente")
            Log.d("Firebase", "✅ Firestore inicializado correctamente")
            Log.d("Firebase", "Usuario actual: ${auth.currentUser?.email ?: "No hay usuario"}")

        } catch (e: Exception) {
            Log.e("Firebase", "❌ Error al inicializar Firebase: ${e.message}")
        }

        setContent {
            EventPlannerrTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "EventPlanner - Firebase Ready! 🔥")
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    EventPlannerrTheme {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "EventPlanner")
        }
    }
}
