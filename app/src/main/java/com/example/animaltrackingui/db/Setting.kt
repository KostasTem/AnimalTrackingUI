package com.example.animaltrackingui.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("setting")
data class Setting(
    @PrimaryKey(autoGenerate = false)
    val name:String,
    val category: SettingCategory,
    var selected:String,
    val type:String,
    val options:List<String>
){
    constructor(): this(name="",SettingCategory.None,selected = "", type="", options = mutableListOf())
}

enum class SettingCategory{
    None, Notification, Localization, Undefined
}