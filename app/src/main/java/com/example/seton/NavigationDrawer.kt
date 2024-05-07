package com.example.seton

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
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
    modifier: Modifier = Modifier,
    itemTextStyle: TextStyle = TextStyle(fontSize = 18.sp),
    onItemClick: (MenuItem) -> Unit
) {
    LazyColumn(modifier){
        items(items){item ->
            Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        onItemClick(item)
                    }
                    .padding(16.dp)
                    .background(if(item.isSelected) Color.LightGray else Color.Transparent)
            ){
                Icon(
                    imageVector = item.icon,
                    contentDescription = item.contentDescription
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = item.title,
                    style = itemTextStyle,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}
//fun DrawerBody(
//    items: List<MenuItem>,
//    modifier: Modifier = Modifier,
//    itemTextStyle: TextStyle = TextStyle(fontSize = 18.sp),
//    onItemClick: (MenuItem) -> Unit
//) {
//    var selectedItem = remember { mutableStateOf<MenuItem?>(null) }
//
//    LazyColumn(modifier) {
//        items(items) { item ->
//            MenuItemRow(
//                item = item,
//                isSelected = selectedItem.value != null && selectedItem.value == item,
//                onItemClick = { onItemClick(item); selectedItem.value = item }
//            )
//        }
//    }
//}
//
//@Composable
//private fun MenuItemRow(
//    item: MenuItem,
//    isSelected: Boolean,
//    onItemClick: () -> Unit,
//    modifier: Modifier = Modifier
//) {
//    val ripple = rememberRipple()
//    val interactionSource = remember { MutableInteractionSource() }
//    Row(
//        modifier = modifier
//            .fillMaxWidth()
//            .padding(16.dp)
//            .background(if (isSelected) Color.LightGray else Color.Transparent, shape = MaterialTheme.shapes.medium)
//            .clickable(interactionSource, indication = ripple, onClick = onItemClick)
//    ) {
//        Icon(
//            imageVector = item.icon,
//            contentDescription = item.contentDescription
//        )
//        Spacer(modifier = Modifier.width(16.dp))
//        Text(
//            text = item.title,
//            style = TextStyle(fontSize = 18.sp),
//            modifier = Modifier.weight(1f)
//        )
//    }
//}