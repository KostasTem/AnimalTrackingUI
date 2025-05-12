package com.example.animaltrackingui.db

data class LoginRequest(val email: String?, val password: String?, val deviceID: String, val googleToken: String?, val staySignedIn: Boolean)
