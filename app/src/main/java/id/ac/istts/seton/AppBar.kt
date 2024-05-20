package id.ac.istts.seton

import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
fun AppBar(
    name : String,
    onNavigationIconClick: () -> Unit
) {
    TopAppBar (
        title = {
            Text(text = name)
        },
        backgroundColor = Color.White,
        contentColor =  Color(0xFF0E9794),
        navigationIcon = {
            IconButton(onClick = onNavigationIconClick) {
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = "Toggle drawer"
                )
            }
        }
    )
}