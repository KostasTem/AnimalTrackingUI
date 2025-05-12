package com.example.animaltrackingui.db

import com.example.animaltrackingui.R

sealed class BottomNavigation(val route: String, val resource:Int, val label: String){
    object Posts: BottomNavigation("Posts", R.drawable.home,"Posts")
    object PostMap: BottomNavigation("PostMap", R.drawable.map,"Post Map")
    object AddObject: BottomNavigation("AddObject", R.drawable.add, "Add")
}