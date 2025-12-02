package com.tecsup.eventplannerr.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tecsup.eventplannerr.data.AuthRepository
import com.tecsup.eventplannerr.data.EventRepository
import com.tecsup.eventplannerr.model.Event
import com.google.firebase.Timestamp
import kotlinx.coroutines.launch
import java.util.*

@Composable
fun CreateEditEventScreen(
    authRepo: AuthRepository,
    eventRepo: EventRepository,
    onSaved: () -> Unit,
    onCancel: () -> Unit
) {

    // NECESARIO para llamar a funciones suspend
    val scope = rememberCoroutineScope()

    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var dateString by remember { mutableStateOf("") }
    var message by remember { mutableStateOf<String?>(null) }
    var loading by remember { mutableStateOf(false) }

    Column(modifier = Modifier.padding(16.dp)) {

        Text("Crear Evento", style = MaterialTheme.typography.h6)
        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("Título *") }
        )

        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            value = dateString,
            onValueChange = { dateString = it },
            label = { Text("Fecha (YYYY-MM-DD)") }
        )

        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("Descripción (opcional)") }
        )

        Spacer(Modifier.height(12.dp))

        Row {

            Button(
                onClick = {
                    message = null

                    if (title.isBlank()) {
                        message = "El título es obligatorio"
                        return@Button
                    }

                    val uid = authRepo.currentUserUid()
                    if (uid == null) {
                        message = "Usuario no autenticado"
                        return@Button
                    }

                    loading = true

                    // Parse fecha a Timestamp
                    val timestamp = try {
                        val parts = dateString.split("-")
                        val cal = Calendar.getInstance()
                        if (parts.size >= 3) {
                            cal.set(
                                parts[0].toInt(),
                                parts[1].toInt() - 1,
                                parts[2].toInt(),
                                0,
                                0
                            )
                        }
                        Timestamp(cal.time)
                    } catch (e: Exception) {
                        Timestamp(Date())
                    }

                    // LLAMAR A LA FUNCIÓN SUSPEND CON COROUTINA
                    scope.launch {
                        val result = eventRepo.createEvent(
                            Event(
                                title = title,
                                description = description,
                                dateTimestamp = timestamp,
                                ownerUid = uid
                            )
                        )

                        loading = false

                        if (result.isSuccess) {
                            onSaved()
                        } else {
                            message =
                                result.exceptionOrNull()?.message ?: "Error al crear el evento"
                        }
                    }
                },
                enabled = !loading
            ) {
                Text("Guardar")
            }

            Spacer(Modifier.width(8.dp))

            OutlinedButton(onClick = onCancel) {
                Text("Cancelar")
            }
        }

        message?.let {
            Text(it, color = MaterialTheme.colors.error)
        }
    }
}
