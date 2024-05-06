package com.example.seton.mainPage

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.ListAlt
import androidx.compose.material.icons.filled.Report
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Task
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.rememberCoroutineScope
import com.example.seton.MenuItem
import kotlinx.coroutines.launch

class DashboardActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val scaffoldState = rememberScaffoldState()
            val scope = rememberCoroutineScope()
            Scaffold(
                scaffoldState = scaffoldState,
                topBar = {
                    AppBar(
                        onNavigationIconClick = {
                            scope.launch {
                                scaffoldState.drawerState.open()
                            }
                        }
                    )
                },
                drawerContent = {
                    DrawerHeader()
                    DrawerBody(
                        items = listOf(
                            MenuItem(
                                id = "dashboard",
                                title = "Dashboard",
                                contentDescription = "Go to dashboard",
                                icon = Icons.Default.Dashboard
                            ),
                            MenuItem(
                                id = "projects",
                                title = "Projects",
                                contentDescription = "Go to projects",
                                icon = Icons.Default.ListAlt
                            ),
                            MenuItem(
                                id = "tasks",
                                title = "Tasks",
                                contentDescription = "Go to tasks",
                                icon = Icons.Default.Task
                            ),
                            MenuItem(
                                id = "calendar",
                                title = "Calendar",
                                contentDescription = "Go to calendar",
                                icon = Icons.Default.CalendarToday
                            ),
                            MenuItem(
                                id = "report",
                                title = "Report",
                                contentDescription = "Go to report",
                                icon = Icons.Default.Report
                            ),
                            MenuItem(
                                id = "setting",
                                title = "Setting",
                                contentDescription = "Go to setting",
                                icon = Icons.Default.Settings
                            ),
                        ),
                        onItemClick = {
                            println("Clicked ${it.title}")
//                                when(it.title){
//
//                                }
                        }
                    )
                }
            ) {
                //asdasdasdasd
            }
        }
    }
}

