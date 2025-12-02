package com.tecsup.eventplannerr.ui.screens

import android.util.Patterns
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tecsup.eventplannerr.data.AuthRepository
import kotlinx.coroutines.launch

@Composable
fun RegisterScreen(authRepo: AuthRepository, onRegisterSuccess: () -> Unit, onBack: () -> Unit) {
    val scope = rememberCoroutineScope()
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirm by remember { mutableStateOf("") }
    var message by remember { mutableStateOf<String?>(null) }
    var loading by remember { mutableStateOf(false) }

    Column(modifier = Modifier.padding(16.dp)) {
        Text("Registro", style = MaterialTheme.typography.h6)
        Spacer(Modifier.height(12.dp))
        OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("Email") })
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(value = password, onValueChange = { password = it }, label = { Text("Contraseña") }, singleLine = true)
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(value = confirm, onValueChange = { confirm = it }, label = { Text("Confirmar Contraseña") }, singleLine = true)
        Spacer(Modifier.height(12.dp))
        Button(onClick = {
            message = null
            if (email.isBlank() || password.isBlank() || confirm.isBlank()) {
                message = "Completa todos los campos"
                return@Button
            }
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                message = "El formato del correo electrónico no es válido"
                return@Button
            }
            if (password != confirm) {
                message = "Las contraseñas no coinciden"
                return@Button
            }
            loading = true
            scope.launch {
                val res = authRepo.register(email, password)
                loading = false
                if (res.isSuccess) onRegisterSuccess() else message = res.exceptionOrNull()?.message ?: "Error"
            }
        }, enabled = !loading) {
            Text("Registrarme")
        }
        Spacer(Modifier.height(8.dp))
        OutlinedButton(onClick = onBack) {
            Text("Volver")
        }
        message?.let { Text(it, color = MaterialTheme.colors.error) }
    }
}
