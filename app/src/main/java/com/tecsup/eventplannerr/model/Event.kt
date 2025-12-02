package com.tecsup.eventplannerr.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import java.util.*

data class Event(
    @DocumentId
    var id: String = "",
    var title: String = "",
    var description: String? = "",
    var dateTimestamp: Timestamp? = null,
    var ownerUid: String = ""
)
