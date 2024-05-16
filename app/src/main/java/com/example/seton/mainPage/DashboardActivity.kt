package com.example.seton.mainPage

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Card
import androidx.compose.material3.DrawerValue
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.ListAlt
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Report
import androidx.compose.material.icons.filled.Task
import androidx.compose.material.icons.outlined.CalendarToday
import androidx.compose.material.icons.outlined.Dashboard
import androidx.compose.material.icons.outlined.ListAlt
import androidx.compose.material.icons.outlined.Logout
import androidx.compose.material.icons.outlined.Report
import androidx.compose.material.icons.outlined.Task
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.seton.AppBar
import com.example.seton.DrawerBody
import com.example.seton.DrawerHeader
import com.example.seton.MenuItem
import com.example.seton.Screens
import com.example.seton.config.ApiConfiguration
//import com.example.seton.SetUpNavGraph
import com.example.seton.loginRegister.LoginActivity
import com.example.seton.projectPage.ListProjectActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DashboardActivity : ComponentActivity() {
    lateinit var userEmail : String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userEmail = intent.getStringExtra("userEmail").toString()
        setContent {
            val items = listOf(
                MenuItem(
                    title = "Dashboard",
                    route = Screens.Dashboard.route,
                    selectedIcon = Icons.Filled.Dashboard,
                    unSelectedIcon = Icons.Outlined.Dashboard
                ),
                MenuItem(
                    title = "Projects",
                    route = Screens.Projects.route,
                    selectedIcon = Icons.Filled.ListAlt,
                    unSelectedIcon = Icons.Outlined.ListAlt
                ),
                MenuItem(
                    title = "Tasks",
                    route = Screens.Tasks.route,
                    selectedIcon = Icons.Filled.Task,
                    unSelectedIcon = Icons.Outlined.Task
                ),
                MenuItem(
                    title = "Calendar",
                    route = Screens.Calendar.route,
                    selectedIcon = Icons.Filled.CalendarToday,
                    unSelectedIcon = Icons.Outlined.CalendarToday
                ),
                MenuItem(
                    title = "Report",
                    route = Screens.Report.route,
                    selectedIcon = Icons.Filled.Report,
                    unSelectedIcon = Icons.Outlined.Report
                ),
                MenuItem(
                    title = "Logout",
                    route = Screens.Logout.route,
                    selectedIcon = Icons.Filled.Logout,
                    unSelectedIcon = Icons.Outlined.Logout
                ),
            )

            val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
            val scope = rememberCoroutineScope()
//            val navController = rememberNavController()
//            val navBackStackEntry by navController.currentBackStackEntryAsState()
//            val currentRoute = navBackStackEntry?.destination?.route

//            val topBarTitle =
//                if (currentRoute != null){
//                    items[items.indexOfFirst {
//                        it.route == currentRoute
//                    }].title
//                }
//                else {
//                    items[0].title
//                }

            ModalNavigationDrawer(
                gesturesEnabled = drawerState.isOpen,
                drawerContent = {
                    ModalDrawerSheet {
                        DrawerHeader()
                        Spacer(modifier = Modifier.height(8.dp))
                        DrawerBody(
                            items = items,
                            onItemClick = { currentMenuItem ->
                                when (currentMenuItem.route){
                                    Screens.Logout.route -> {
                                        val ioScope = CoroutineScope(Dispatchers.Main)
                                        ioScope.launch {
                                            ApiConfiguration.defaultRepo.logoutUser()
                                        }
                                        startActivity(Intent(this@DashboardActivity, LoginActivity::class.java))
                                    }
                                    Screens.Projects.route -> {
                                        startActivity(
                                            Intent(this@DashboardActivity, ListProjectActivity::class.java)
                                                .putExtra("userEmail", userEmail)
                                        )
                                    }
                                }
                            }
                        )
                    }
                }, drawerState = drawerState
            ) {
                Scaffold(
                    topBar = {
                        AppBar (
                            onNavigationIconClick = {
                                scope.launch {
                                    drawerState.open()
                                }
                            }
                        )
                    }
                ) {
                    val hai = it
                    chartPreview()
                }
            }
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun chartPreview() {
        ConstraintLayout(modifier = Modifier
            .fillMaxSize()
            .background(Color.White)) {
            LazyColumn(
                Modifier
                    .fillMaxSize()
                    .padding(16.dp, 60.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ){
                item {
                    chartItem()
                }
            }
        }
    }

    @Composable
    fun chartItem() {
        Column {
            Text(
                text = "Weekly Stats",
                style = MaterialTheme.typography.h6,
                modifier = Modifier.padding(2.dp, 10.dp)
            )
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                Card(
                    elevation = 8.dp,
                    backgroundColor = Color.White,
                ) {
                    Chart(
                        data = mapOf(
                            Pair(0.5f, 10),
                            Pair(0.6f, 12),
                            Pair(0.2f, 13),
                            Pair(0.7f, 15),
                            Pair(0.8f, 16),
                        ),
                        max_value = 6,
                        days = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")
                    )
                }
            }
        }
    }
}