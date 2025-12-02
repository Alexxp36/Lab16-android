package com.tecsup.eventplannerr.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tecsup.eventplannerr.data.AuthRepository
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(authRepo: AuthRepository, onLoginSuccess: () -> Unit, onNavigateRegister: () -> Unit) {
    val scope = rememberCoroutineScope()
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var message by remember { mutableStateOf<String?>(null) }
    var loading by remember { mutableStateOf(false) }

    Column(modifier = Modifier.padding(16.dp)) {
        Text("EventPlanner - Login", style = MaterialTheme.typography.h6)
        Spacer(Modifier.height(12.dp))
        OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("Email") })
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(value = password, onValueChange = { password = it }, label = { Text("Contraseña") }, singleLine = true)
        Spacer(Modifier.height(12.dp))
        Button(onClick = {
            message = null
            if (email.isBlank() || password.isBlank()) {
                message = "Completa todos los campos"
                return@Button
            }
            loading = true
            scope.launch {
                val res = authRepo.login(email, password)
                loading = false
                if (res.isSuccess) onLoginSuccess() else message = res.exceptionOrNull()?.message ?: "Error"
            }
        }, enabled = !loading) {
            Text("Iniciar Sesión")
        }
        Spacer(Modifier.height(8.dp))
        OutlinedButton(onClick = onNavigateRegister) {
            Text("Registrarme")
        }
        message?.let { Text(it, color = MaterialTheme.colors.error) }
    }
}
