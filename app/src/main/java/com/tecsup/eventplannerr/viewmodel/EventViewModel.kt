package com.tecsup.eventplannerr.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope


import com.google.firebase.Timestamp
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import com.google.firebase.firestore.ListenerRegistration
import com.tecsup.eventplannerr.data.EventRepository
import com.tecsup.eventplannerr.model.Event

sealed class EventsUiState {
    object Loading: EventsUiState()
    data class Success(val list: List<Event>): EventsUiState()
    data class Error(val message: String): EventsUiState()
}

class EventViewModel(private val repo: EventRepository): ViewModel() {

    private val _uiState = MutableStateFlow<EventsUiState>(EventsUiState.Loading)
    val uiState: StateFlow<EventsUiState> = _uiState

    private var listener: ListenerRegistration? = null

    fun startListening(uid: String) {
        _uiState.value = EventsUiState.Loading
        listener?.remove()
        listener = repo.listenUserEvents(uid) { list ->
            _uiState.value = EventsUiState.Success(list)
        }
    }

    fun stopListening() {
        listener?.remove()
        listener = null
    }

    fun createEvent(title: String, description: String?, date: Timestamp, ownerUid: String, onComplete: (Result<String>) -> Unit) {
        viewModelScope.launch {
            val event = Event(title = title, description = description, dateTimestamp = date, ownerUid = ownerUid)
            val result = repo.createEvent(event)
            onComplete(result)
        }
    }

    fun updateEvent(event: Event, onComplete: (Result<Unit>) -> Unit) {
        viewModelScope.launch {
            val result = repo.updateEvent(event)
            onComplete(result)
        }
    }

    fun deleteEvent(eventId: String, onComplete: (Result<Unit>) -> Unit) {
        viewModelScope.launch {
            val result = repo.deleteEvent(eventId)
            onComplete(result)
        }
    }
}
