package id.ac.istts.seton

import androidx.compose.ui.graphics.vector.ImageVector

data class MenuItem(
    val title : String,
    val route : String,
    val selectedIcon : ImageVector,
    val unSelectedIcon : ImageVector,
    val badgeCount : Int? = null
)
