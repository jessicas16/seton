package id.ac.istts.seton

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

@Composable
fun DrawerHeader() {
    Column(
        modifier = Modifier
            .fillMaxWidth().wrapContentHeight()
            .padding(vertical = 50.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
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
            selected = currentRoute == menuItem.route, onClick = {
                onItemClick(menuItem)
            },
//            onClick = {
//                onItemClick(menuItem)
//            },
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