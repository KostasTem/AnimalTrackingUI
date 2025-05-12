package com.example.animaltrackingui.services

import android.content.Context
import android.widget.Toast

class ToastService(private val context: Context) {

    fun displayToast(text: String){
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
    }

}