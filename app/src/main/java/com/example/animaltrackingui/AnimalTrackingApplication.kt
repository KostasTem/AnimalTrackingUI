package com.example.animaltrackingui

import android.app.Application
import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.initialize

class AnimalTrackingApplication: Application() {
    private val tag = "AnimalTracking"

    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        Firebase.initialize(this)
        container = DefaultAppContainer(this)
        container.init()
    }

    override fun onTerminate() {
        Log.i(tag,"onTerminate")
        super.onTerminate()
    }
}