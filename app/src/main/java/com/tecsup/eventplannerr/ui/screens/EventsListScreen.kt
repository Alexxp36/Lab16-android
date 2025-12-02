package com.tecsup.eventplannerr.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tecsup.eventplannerr.data.AuthRepository
import com.tecsup.eventplannerr.data.EventRepository
import com.tecsup.eventplannerr.model.Event
import kotlinx.coroutines.launch   // ← IMPORT NECESARIO PARA scope.launch

@Composable
fun EventsListScreen(
    authRepo: AuthRepository,
    eventRepo: EventRepository,
    onCreateEvent: () -> Unit,
    onEditEvent: (String) -> Unit,
    onSignOut: () -> Unit
) {

    // 🔥 NECESARIO PARA usar scope.launch al eliminar
    val scope = rememberCoroutineScope()

    val currentUid = authRepo.currentUserUid()
    var events by remember { mutableStateOf<List<Event>>(emptyList()) }
    var loading by remember { mutableStateOf(false) }
    var message by remember { mutableStateOf<String?>(null) }

    DisposableEffect(currentUid) {
        if (currentUid != null) {
            loading = true
            val listener = eventRepo.listenUserEvents(currentUid) { list ->
                events = list
                loading = false
            }
            onDispose {
                listener.remove()
            }
        } else {
            onDispose { }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Mis eventos", style = MaterialTheme.typography.h6)
            Button(
                onClick = {
                    authRepo.logout()
                    onSignOut()
                }
            ) {
                Text("Cerrar sesión")
            }
        }

        Spacer(Modifier.height(12.dp))

        Button(onClick = onCreateEvent) {
            Text("Crear evento")
        }

        Spacer(Modifier.height(12.dp))

        if (loading) {
            CircularProgressIndicator()
        } else {
            if (events.isEmpty()) {
                Text("No hay eventos aún.")
            } else {
                LazyColumn {
                    items(events) { event ->
                        EventItem(
                            event = event,
                            onEdit = { onEditEvent(event.id) },
                            onDelete = {
                                scope.launch {
                                    val res = eventRepo.deleteEvent(event.id)
                                    if (res.isFailure) {
                                        message = res.exceptionOrNull()?.message
                                            ?: "Error al eliminar"
                                    }
                                }
                            }
                        )
                    }
                }
            }
        }

        message?.let {
            Text(
                it,
                color = MaterialTheme.colors.error
            )
        }
    }
}

@Composable
fun EventItem(
    event: Event,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Column(modifier = Modifier.padding(8.dp)) {

            Text(
                text = event.title,
                style = MaterialTheme.typography.subtitle1
            )

            Spacer(Modifier.height(4.dp))

            Text(
                text = event.description ?: "",
                style = MaterialTheme.typography.body2
            )

            Spacer(Modifier.height(4.dp))

            Text(
                text = event.dateTimestamp?.toDate()?.toString() ?: "Sin fecha",
                style = MaterialTheme.typography.caption
            )

            Spacer(Modifier.height(8.dp))

            Row {
                OutlinedButton(onClick = onEdit) {
                    Text("Editar")
                }
                Spacer(Modifier.width(8.dp))
                OutlinedButton(onClick = onDelete) {
                    Text("Eliminar")
                }
            }
        }
    }
}
