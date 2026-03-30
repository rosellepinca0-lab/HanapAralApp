package com.hanapAral.app

import android.app.Application
import com.google.firebase.FirebaseApp

class HanapAralApp : Application() {
    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
    }
}
