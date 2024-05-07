package com.example.seton.mainPage

import android.content.Intent
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
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.ListAlt
import androidx.compose.material.icons.filled.Report
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Task
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.seton.AppBar
import com.example.seton.DrawerBody
import com.example.seton.DrawerHeader
import com.example.seton.MenuItem
import com.example.seton.loginRegister.LoginActivity
import com.example.seton.projectPage.ListProjectActivity
import kotlinx.coroutines.launch

class DashboardActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val scaffoldState = rememberScaffoldState()
            val scope = rememberCoroutineScope()
            val context = LocalContext.current
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
                            when(it.id){
                                "projects" -> {
                                    val intent = Intent(context, ListProjectActivity::class.java)
                                    startActivity(intent)
                                }
                                "logout" -> {
                                    val intent = Intent(context, LoginActivity::class.java)
                                    startActivity(intent)
                                }
                            }
                        }
                    )
                }
            ) {
                val hai = it
                Dashboard()
            }
        }
    }
    @Composable
    fun Dashboard() {
        Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
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

