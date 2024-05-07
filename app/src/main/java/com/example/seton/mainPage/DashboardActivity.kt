package com.example.seton.mainPage

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DrawerValue
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.seton.AppBar
import com.example.seton.DrawerBody
import com.example.seton.DrawerHeader
import com.example.seton.MenuItem
import com.example.seton.Screens
import com.example.seton.SetUpNavGraph
import kotlinx.coroutines.launch

class DashboardActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
            val navController = rememberNavController()
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = navBackStackEntry?.destination?.route

            val topBarTitle =
                if (currentRoute != null){
                    items[items.indexOfFirst {
                        it.route == currentRoute
                    }].title
                }
                else {
                    items[0].title
                }

            ModalNavigationDrawer(
                gesturesEnabled = drawerState.isOpen,
                drawerContent = {
                    ModalDrawerSheet {
                        DrawerHeader()
                        Spacer(modifier = Modifier.height(8.dp))
                        DrawerBody(
                            items = items,
                            currentRoute = currentRoute
                        ){
                            currentMenuItem ->
                                navController.navigate(currentMenuItem.route){
                                    navController.graph.startDestinationRoute?.let {
                                        startDestinationRoute ->
                                            popUpTo(startDestinationRoute){
                                                saveState = true
                                            }
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            scope.launch {
                                drawerState.close()
                            }
                        }
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
                ) { innerPadding ->
                    SetUpNavGraph(navController = navController, innerPadding = innerPadding)
                }
                Dashboard()
            }
        }
    }
    @Composable
    fun Dashboard() {
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)) {
            Text(text = "Weekly Stats", style = MaterialTheme.typography.h4, color = Color.White)
            Spacer(modifier = Modifier.height(16.dp))
            BarChart()
            TaskSummary(upcoming = 15, completed = 11, ongoing = 3)
        }
    }

    @Composable
    fun BarChart() {
        // Dummy data
        val data = listOf(0, 2, 3, 0, 0, 0, 0)
        val daysOfWeek = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")

        Row(horizontalArrangement = Arrangement.SpaceEvenly) {
            data.forEachIndexed { index, value ->
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = value.toString(), color = Color.White)
                    Box(
                        modifier = Modifier
                            .width(10.dp)
                            .height((value * 10).dp)
                            .background(Color.Blue)
                    )
                    Text(text = daysOfWeek[index], color = Color.White)
                }
            }
        }
    }

    @Composable
    fun TaskSummary(upcoming: Int, completed: Int, ongoing: Int) {
        Row(horizontalArrangement = Arrangement.SpaceEvenly) {
            SummaryItem(title = "Upcoming", count = upcoming, color = Color(0xFFFB8C00))
            SummaryItem(title = "Completed", count = completed, color = Color(0xFFC0CA33))
            SummaryItem(title = "Ongoing", count = ongoing, color = Color.Gray)
        }
    }

    @Composable
    fun SummaryItem(title: String, count: Int, color: Color) {
        Surface(color = color, shape = RoundedCornerShape(8.dp), modifier = Modifier.padding(8.dp)) {
            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(16.dp)) {
                Text(text = title, style = MaterialTheme.typography.body1, color = Color.White)
                Text(text = count.toString(), style = MaterialTheme.typography.h6, color = Color.White)
            }
        }
    }
}

