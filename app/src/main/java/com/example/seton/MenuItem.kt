package com.example.seton

import androidx.compose.ui.graphics.vector.ImageVector

data class MenuItem(
//    val id: String,
//    val title: String,
//    val contentDescription: String,
//    val icon: ImageVector,
//    var isSelected: Boolean = false
    val title : String,
    val route : String,
    val selectedIcon : ImageVector,
    val unSelectedIcon : ImageVector,
    val badgeCount : Int? = null
)
