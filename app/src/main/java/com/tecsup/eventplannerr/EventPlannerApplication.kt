package com.tecsup.eventplannerr

import android.app.Application
import com.google.firebase.FirebaseApp

class EventPlannerApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
    }
}
