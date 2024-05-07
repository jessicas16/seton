package com.example.seton

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.seton.MenuItem
import com.example.seton.R
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp

@Composable
fun DrawerHeader() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 64.dp),
        contentAlignment = Alignment.Center
    ){
        Image(
            painter = painterResource(R.drawable.seton_logo),
            contentDescription = "Seton Icon",
        )
    }
}

@Composable
fun DrawerBody(
    items: List<MenuItem>,
    currentRoute: String?,
    onItemClick: (MenuItem) -> Unit,
) {
    items.forEachIndexed { index, menuItem ->
        NavigationDrawerItem(
            label = {
                    Text(text = menuItem.title)
            },
            selected = currentRoute == menuItem.route,
            onClick = {
                onItemClick(menuItem)
            },
            icon = {
                Icon(
                    imageVector = if(currentRoute == menuItem.route){
                        menuItem.selectedIcon
                    }
                    else {
                         menuItem.unSelectedIcon
                    },
                    contentDescription = menuItem.title
                )
            },
            badge = {
                menuItem.badgeCount?.let {
                    Text(text = it.toString())
                }
            },
            modifier = Modifier.padding(
                PaddingValues(horizontal = 12.dp, vertical = 8.dp)
            )
        )
    }
}