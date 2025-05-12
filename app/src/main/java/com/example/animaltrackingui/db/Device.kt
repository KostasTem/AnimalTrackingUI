package com.example.animaltrackingui.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.fasterxml.jackson.annotation.JsonIgnore

@Entity("device")
data class Device(
    @PrimaryKey(false)
    val id: Long,
    var deviceID: String,
    val name: String,
    val version: String,
    val subscribedTopics: List<String>,
    var countryCode: String,
    @JsonIgnore
    var uploaded: Boolean = false,
    @JsonIgnore
    var initialized: Boolean = false
)
{
    constructor() : this(0,"","","",listOf(""), getListOfCountries().first().countryCode)
}