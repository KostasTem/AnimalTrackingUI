package com.example.animaltrackingui

import android.content.Context
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

object LocationProvider {

    lateinit var provider:FusedLocationProviderClient

    fun init(context: Context){
        provider = LocationServices.getFusedLocationProviderClient(context)
    }



}