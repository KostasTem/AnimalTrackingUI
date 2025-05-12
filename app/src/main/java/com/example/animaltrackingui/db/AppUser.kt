package com.example.animaltrackingui.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(tableName = "user")
data class AppUser(
    var username: String?,
    @PrimaryKey(false)
    var email: String,
    var password: String?,
    var location: List<String>?,
    var age: String?,
    var roles: List<String>,
    var imagePath: String? = null,
    var countryCode: String = "",
    var token: String = "",
    var firebaseAuthToken: String = "",
    var accountVerified: Boolean = false,
    var deviceSynced: Boolean = false
)
{
    constructor() : this("","","",listOf(""), LocalDate.now().toString(), listOf(""))
}
